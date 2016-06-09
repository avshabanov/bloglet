package bloglet.website;

import com.truward.brikar.server.launcher.StandardLauncher;

/**
 * Entry point.
 *
 * @author Alexander Shabanov
 */
public final class Launcher {
  public static void main(String[] args) throws Exception {
    try (final StandardLauncher launcher = new StandardLauncher("classpath:/blogletWebsite/")) {
      launcher
          .setSpringSecurityEnabled(true)
          .setSessionsEnabled(true)
          .setStaticHandlerEnabled(true)
          .start();
    }
  }
}
