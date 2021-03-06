/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.runners.dataflow.util;

import org.apache.beam.runners.dataflow.options.DataflowPipelineDebugOptions;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.PipelineOptions;

import com.google.api.services.dataflow.model.DataflowPackage;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Utility class for staging files to GCS.
 */
public class GcsStager implements Stager {
  private DataflowPipelineOptions options;

  private GcsStager(DataflowPipelineOptions options) {
    this.options = options;
  }

  public static GcsStager fromOptions(PipelineOptions options) {
    return new GcsStager(options.as(DataflowPipelineOptions.class));
  }

  @Override
  public List<DataflowPackage> stageFiles() {
    Preconditions.checkNotNull(options.getStagingLocation());
    List<String> filesToStage = options.getFilesToStage();
    String windmillBinary =
        options.as(DataflowPipelineDebugOptions.class).getOverrideWindmillBinary();
    if (windmillBinary != null) {
      filesToStage.add("windmill_main=" + windmillBinary);
    }
    return PackageUtil.stageClasspathElements(
        options.getFilesToStage(), options.getStagingLocation());
  }
}
