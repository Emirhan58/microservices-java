# Observability Stack Setup

This document explains the updated Grafana observability stack for local development.

## Components

### 1. **Grafana Loki** (Logs)
- Uses official Grafana Loki Helm chart
- Configured in **SimpleScalable** mode for local/dev environments
- Includes MinIO for object storage
- 7-day log retention

**Chart Location**: `grafana-loki/`

### 2. **Grafana Tempo** (Traces)
- Uses official Grafana Tempo Helm chart
- Configured as **single binary** for simplicity
- Supports OTLP, Jaeger, and Zipkin protocols
- 7-day trace retention

**Chart Location**: `grafana-tempo/`

### 3. **Grafana Alloy** (Log & Trace Collector)
- Uses official Grafana Alloy Helm chart
- Runs as DaemonSet to collect logs from all pods
- Automatically discovers and scrapes Kubernetes pod logs
- Receives traces via OTLP and forwards to Tempo
- Extracts trace IDs from logs for correlation

**Chart Location**: `grafana-alloy/`

### 4. **Grafana** (Visualization)
- Custom lightweight chart
- Pre-configured datasources for Prometheus, Loki, and Tempo
- Trace-to-log correlation enabled

**Chart Location**: `grafana/`

## Installation Order

Install in the following order to ensure dependencies are met:

```bash
# 1. Update Helm dependencies
cd grafana-loki
helm dependency update
cd ../grafana-tempo
helm dependency update
cd ../grafana-alloy
helm dependency update
cd ..

# 2. Install Loki (logs storage)
helm install grafana-loki ./grafana-loki

# 3. Install Tempo (traces storage)
helm install grafana-tempo ./grafana-tempo

# 4. Install Alloy (collector)
helm install grafana-alloy ./grafana-alloy

# 5. Install Grafana (visualization)
helm install grafana ./grafana
```

## Service Endpoints

After installation, the following services will be available:

- **Grafana UI**: `http://localhost:3000` (if using LoadBalancer or port-forward)
  - Username: `admin`
  - Password: `admin`

- **Loki Gateway**: `http://grafana-loki-gateway.default.svc.cluster.local`
- **Tempo**: `http://grafana-tempo.default.svc.cluster.local:3100`
- **Alloy OTLP Receiver**: `http://grafana-alloy.default.svc.cluster.local:4317` (gRPC)

## How It Works

### Log Collection Flow
1. Alloy DaemonSet runs on each node
2. Discovers all Kubernetes pods automatically
3. Reads container logs from `/var/log/pods`
4. Extracts metadata (namespace, pod, container, app labels)
5. Parses JSON logs and extracts trace IDs
6. Pushes logs to Loki via the Gateway

### Trace Collection Flow
1. Your applications send traces via OTLP to Alloy (port 4317/4318)
2. Alloy forwards traces to Tempo
3. Tempo stores traces and makes them queryable

### Correlation
- Logs containing `trace_id` field are automatically linked to traces
- In Grafana, you can jump from logs to traces and vice versa

## Application Configuration

To send logs and traces from your microservices:

### Logs
Your application logs should be in JSON format with trace ID:
```json
{
  "timestamp": "2024-10-06T12:00:00Z",
  "level": "INFO",
  "message": "Processing request",
  "trace_id": "abc123xyz"
}
```

Alloy will automatically collect these logs from stdout/stderr.

### Traces
Configure your application to send OTLP traces to:
- **Endpoint**: `http://grafana-alloy.default.svc.cluster.local:4317`
- **Protocol**: gRPC (OTLP)

For Spring Boot with Micrometer Tracing:
```yaml
management:
  otlp:
    tracing:
      endpoint: http://grafana-alloy.default.svc.cluster.local:4317
```

## Verification

Check that all components are running:
```bash
kubectl get pods | grep -E "grafana-loki|grafana-tempo|grafana-alloy|grafana"
```

Expected pods:
- `grafana-loki-backend-*`
- `grafana-loki-read-*`
- `grafana-loki-write-*`
- `grafana-loki-gateway-*`
- `grafana-loki-minio-*`
- `grafana-tempo-*`
- `grafana-alloy-*` (one per node)
- `grafana-*`

## Troubleshooting

### Check Alloy Configuration
```bash
kubectl logs -l app.kubernetes.io/name=alloy
```

### Check Loki Ingestion
```bash
kubectl logs -l app.kubernetes.io/component=write
```

### Check Tempo Ingestion
```bash
kubectl logs -l app.kubernetes.io/name=tempo
```

### Access Grafana Explore
1. Port-forward Grafana: `kubectl port-forward svc/grafana 3000:3000`
2. Open http://localhost:3000
3. Go to Explore
4. Select Loki datasource to query logs
5. Select Tempo datasource to query traces

## Resource Requirements

Minimum for local development:
- **CPU**: ~1 core total
- **Memory**: ~2GB total
- **Storage**: ~25GB PVs

This configuration is optimized for local development. For production, you would need:
- Multiple replicas
- External object storage (S3, GCS, Azure Blob)
- Proper resource limits
- High availability configuration
