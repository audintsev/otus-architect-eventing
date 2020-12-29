{{/*
Expand the name of the chart.
*/}}
{{- define "arch-eventing.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "arch-eventing.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "arch-eventing.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "arch-eventing.labels.order" -}}
helm.sh/chart: {{ include "arch-eventing.chart" . }}
{{ include "arch-eventing.selectorLabels.order" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "arch-eventing.labels.billing" -}}
helm.sh/chart: {{ include "arch-eventing.chart" . }}
{{ include "arch-eventing.selectorLabels.billing" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "arch-eventing.labels.notification" -}}
helm.sh/chart: {{ include "arch-eventing.chart" . }}
{{ include "arch-eventing.selectorLabels.notification" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "arch-eventing.selectorLabels.order" -}}
app.kubernetes.io/name: {{ include "arch-eventing.name" . }}-order
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
{{- define "arch-eventing.selectorLabels.billing" -}}
app.kubernetes.io/name: {{ include "arch-eventing.name" . }}-billing
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
{{- define "arch-eventing.selectorLabels.notification" -}}
app.kubernetes.io/name: {{ include "arch-eventing.name" . }}-notification
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "arch-eventing.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "arch-eventing.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "arch-eventing.db.fullname" -}}
{{- if .Values.postgresql.fullnameOverride -}}
{{- .Values.postgresql.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default "postgresql" .Values.postgresql.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end }}

{{- define "arch-eventing.rabbitmq.fullname" -}}
{{- if .Values.rabbitmq.fullnameOverride -}}
{{- .Values.rabbitmq.fullnameOverride | trunc 20 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default "rabbitmq" .Values.rabbitmq.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 20 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 20 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}
