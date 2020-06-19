package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.olingo.server.api.debug.RuntimeMeasurement;

import java.util.Collection;

public interface JPAServiceDebugger {
  int startRuntimeMeasurement(final Object instance, final String methodName);

  void stopRuntimeMeasurement(final int handle);

  Collection<RuntimeMeasurement> getRuntimeInformation();
}