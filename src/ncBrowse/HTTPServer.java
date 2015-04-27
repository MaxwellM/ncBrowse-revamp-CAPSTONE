package ncBrowse;

/**
 * An example of a very simple, multi-threaded HTTP server.
 * Originally from Sun Microsystems tutorial
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer {

  /* static class data/methods */

  /* print to stdout */
  protected static void p(String s) {
    System.out.println(s);
  }

  /* print to the log file */
  protected static void log(String s) {
    synchronized (log) {
      log.println(s);
      log.flush();
    }
  }

  static final PrintStream log = null;

  /* Where worker threads stand idle */
  static final Vector threads = new Vector();

  /* the web server's virtual root */
  static File root;

  /* timeout on client connections */
  static int timeout = 0;

  /* max # worker threads */
  static int workers = 5;

  final static int port = 11111;

  static void printProps() {
    p("root=" + root);
    p("timeout=" + timeout);
    p("workers=" + workers);
    p("port=" + port);
  }

  public static void main(String[] args) throws Exception {
    HTTPHandler handler = url -> System.out.println("Got URL:" + url);
    HTTPServer.start(handler);
  }

  public static void start(final HTTPHandler handler) {
    //printProps();
    /* start worker threads */
    for (int i = 0; i < workers; ++i) {
      Worker w = new Worker(handler);
      (new Thread(w, "worker #" + i)).start();
      threads.addElement(w);
    }

    Thread theThread = new Thread() {
      public void run(){
        try {
          ServerSocket ss = new ServerSocket(port, 5, InetAddress.getByName("127.0.0.1"));
          while (true) {

            Socket s = ss.accept();
            Worker w = null;
            synchronized (threads) {
              if (threads.isEmpty()) {
                Worker ws = new Worker(handler);
                ws.setSocket(s);
                (new Thread(ws, "additional worker")).start();
              }
              else {
                w = (Worker) threads.elementAt(0);
                threads.removeElementAt(0);
                w.setSocket(s);
              }
            }
          }
        }
        catch (IOException ex) {
          System.out.println("Start http server: " + ex.getMessage());
//          ex.printStackTrace();
        }
      }
    };
    theThread.start();
  }

  public interface HTTPHandler{
    void handle(URL url);
  }

  static class Worker
      extends HTTPServer
      implements Runnable {
    final static int BUF_SIZE = 8192;

    static final byte[] EOL = {
         (byte) '\r', (byte) '\n'};
     HTTPHandler handler;

    /* buffer to use for requests */
    byte[] buf;
    /* Socket to client we're handling */
    private Socket s;

    Worker(HTTPHandler handler) {
      buf = new byte[BUF_SIZE];
      s = null;
      this.handler = handler;
    }

    synchronized void setSocket(Socket s) {
      this.s = s;
      notify();
    }

    public synchronized void run() {
      while (true) {
        if (s == null) {
          /* nothing to do */
          try {
            wait();
          }
          catch (InterruptedException e) {
            /* should not happen */
            continue;
          }
        }
        try {
          handleClient();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        /* go back in wait queue if there's fewer
         * than numHandler connections.
         */
        s = null;
        Vector pool = HTTPServer.threads;
        synchronized (pool) {
          if (pool.size() >= HTTPServer.workers) {
            /* too many threads, exit this one */
            return;
          }
          else {
            pool.addElement(this);
          }
        }
      }
    }

    void handleClient() throws IOException {
      InputStream is = new BufferedInputStream(s.getInputStream());
      PrintStream ps = new PrintStream(s.getOutputStream());
      /* we will only block in read for this many milliseconds
       * before we fail with java.io.InterruptedIOException,
       * at which point we will abandon the connection.
       */
      s.setSoTimeout(HTTPServer.timeout);
      s.setTcpNoDelay(true);
      /* zero out the buffer from last time */
      for (int i = 0; i < BUF_SIZE; i++) {
        buf[i] = 0;
      }
      int nread = 0;
      try {
        int r = 0;
        boolean finished = false;
        while ( (r = is.read(buf, nread, BUF_SIZE-nread)) > 0 && !finished) {
          nread += r;
          for (int i=0; i < nread; ++i){
            if (buf[i] == (byte)'\n' || buf[i] == (byte)'\r') {
              finished = true;
              break;
            }
          }
          if (finished) break;
        }
        ps.println("HTTP/1.0 200 OK");
        ps.println("Content-Type: text/html");
        ps.println("");
        ps.println(
            "<html><body onload=\"javascript:history.go(-1)\"></body></html>");
        ps.println();
        ps.flush();

      }
      finally {
        s.close();
      }
      String data = new String(buf,0,nread);
      String[] parts = data.split("\\s");
      if (parts.length > 2 && parts[0].equalsIgnoreCase("get")){
        handleUrl(parts[1]);
      }
    }

    void handleUrl(String req){
      try {
        URL url = new URL("http://localhost:" + port + req);
        handler.handle(url);
      }
      catch (MalformedURLException ex) {
        ex.printStackTrace();
      }
    }

  }
}

