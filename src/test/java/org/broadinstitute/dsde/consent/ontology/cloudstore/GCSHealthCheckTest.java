package org.broadinstitute.dsde.consent.ontology.cloudstore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.codahale.metrics.health.HealthCheck;
import com.google.api.services.storage.model.Bucket;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GCSHealthCheckTest {

  private GCSHealthCheck healthCheck;

  @Mock
  private GCSStore store;

  @BeforeEach
  void setUpClass() {
    healthCheck = new GCSHealthCheck(store);
  }

  @Test
  void testBucketExists() throws IOException, GeneralSecurityException {
    when(store.getBucketMetadata()).thenReturn(new Bucket());

    HealthCheck.Result result = healthCheck.execute();
    assertTrue(result.isHealthy());
  }

  @Test
  void testBucketMissing() throws Exception {
    when(store.getBucketMetadata()).thenReturn(null);

    HealthCheck.Result result = healthCheck.execute();
    assertFalse(result.isHealthy());
    assertTrue(result.getMessage().contains("GCS bucket unreachable"));
  }
}
