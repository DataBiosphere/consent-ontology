package org.broadinstitute.dsde.consent.ontology;

import io.sentry.Sentry;
import io.sentry.SentryEvent;

public interface OntologyLogger {

  /**
   * Logs an error message to the console and to Sentry
   *
   * @param message Error Message
   */
  default void logError(String message) {
    Utils.getLogger(this.getClass()).error(message);
    Sentry.captureEvent(new SentryEvent(new Exception(message)));
  }

  /**
   * Logs an exception to the console and to Sentry
   *
   * @param e Exception
   */
  default void logException(Exception e) {
    Utils.getLogger(this.getClass()).error(e.getMessage());
    Sentry.captureEvent(new SentryEvent(e));
  }

  default void logException(String message, Exception e) {
    Utils.getLogger(this.getClass()).error(message + e.getMessage());
    Sentry.captureEvent(new SentryEvent(e));
  }

  /**
   * Logs a warning message to the console
   *
   * @param message Error Message
   */
  default void logWarn(String message) {
    Utils.getLogger(this.getClass()).warn(message);
  }

  /**
   * Logs an info message to the console
   *
   * @param message Error Message
   */
  default void logInfo(String message) {
    Utils.getLogger(this.getClass()).info(message);
  }

  /**
   * Logs a debug message to the console
   *
   * @param message Error Message
   */
  default void logDebug(String message) {
    Utils.getLogger(this.getClass()).debug(message);
  }

}
