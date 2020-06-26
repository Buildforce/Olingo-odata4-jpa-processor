package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.processor.core.api.JPAServiceDebugger;
import nl.buildforce.olingo.server.api.debug.RuntimeMeasurement;

import java.util.ArrayList;
import java.util.Collection;

public final class JPAEmptyDebugger implements JPAServiceDebugger {

  @Override
  public int startRuntimeMeasurement(final Object instance, final String methodName) {
    return 0;
  }

  @Override
  public void stopRuntimeMeasurement(final int handle) {
    // Not needed
  }

  @Override
  public Collection<RuntimeMeasurement> getRuntimeInformation() {
    return new ArrayList<>();
  }
}