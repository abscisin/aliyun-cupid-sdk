/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aliyun.odps.cupid.requestcupid;

import apsara.odps.cupid.protocol.CupidTaskParamProtos;
import com.aliyun.odps.cupid.CupidSession;
import com.aliyun.odps.cupid.utils.JTuple;
import java.util.Map;

/**
 * Created by mingshuo on 1/8/18.
 */
public class UpgradeAppUtil {
  /*
   * @resources resourceName -> resourcePath
   */
  public static void UpgradeApplication(CupidSession cupidSession, Map<String, String> resources) throws Throwable {
    CupidTaskParamProtos.CupidTaskParam.Builder cupidTaskParamBuilder =
            CupidTaskBaseUtil.getOperationBaseInfo(
                    cupidSession,
                    CupidTaskOperatorConst.CUPID_TASK_UPGRADE_APPLICATION,
                    cupidSession.getJobLookupName(),
                    null
            );
    CupidTaskParamProtos.JobConf.Builder confBuilder = CupidTaskParamProtos.JobConf.newBuilder();
    for (Map.Entry<String, String> item : resources.entrySet()) {
      JTuple.JTuple2<String, String> res = CopyTempResourceUtil.addAndCopyTempResource(cupidSession, item.getValue(), cupidSession.odps());
      String path = res._2();
      CupidTaskParamProtos.JobConfItem.Builder itemBuilder = CupidTaskParamProtos.JobConfItem.newBuilder();
      itemBuilder.setKey(item.getKey());
      itemBuilder.setValue(path);
      confBuilder.addJobconfitem(itemBuilder);
    }
    cupidTaskParamBuilder.setJobconf(confBuilder);
    SubmitJobUtil.upgradeApplicationSubmitJob(cupidSession, cupidTaskParamBuilder.build());
  }
}