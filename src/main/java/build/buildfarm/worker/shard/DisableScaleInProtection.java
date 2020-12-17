// Copyright 2019 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package build.buildfarm.worker.shard;

import build.buildfarm.v1test.DisableScaleInProtectionGrpc;
import build.buildfarm.v1test.DisableScaleInProtectionRequest;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

class DisableScaleInProtection {
  private static ManagedChannel createChannel(String target) {
    NettyChannelBuilder builder =
        NettyChannelBuilder.forTarget(target).negotiationType(NegotiationType.PLAINTEXT);
    return builder.build();
  }

  private static ManagedChannel channel;

  private static final Supplier<DisableScaleInProtectionGrpc.DisableScaleInProtectionBlockingStub>
      disableScaleInProtectionBlockingStub =
          Suppliers.memoize(
              new Supplier<DisableScaleInProtectionGrpc.DisableScaleInProtectionBlockingStub>() {
                @Override
                public DisableScaleInProtectionGrpc.DisableScaleInProtectionBlockingStub get() {
                  return DisableScaleInProtectionGrpc.newBlockingStub(channel);
                }
              });

  public static void disableScaleInProtection(String host) {
    channel = createChannel(host);
    disableScaleInProtectionBlockingStub
        .get()
        .disableScaleInProtection(DisableScaleInProtectionRequest.newBuilder().build());
  }
}
