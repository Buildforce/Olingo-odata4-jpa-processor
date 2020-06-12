package nl.buildforce.sequoia.jpa.processor.core.api;

import org.apache.olingo.server.api.debug.RuntimeMeasurement;

import java.util.Collection;

public interface JPAServiceDebugger {
  int startRuntimeMeasurement(final Object instance, final String methodName);

  void stopRuntimeMeasurement(final int handle);

  Collection<RuntimeMeasurement> getRuntimeInformation();
}