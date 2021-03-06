/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.core.payment.payload;

import io.bisq.generated.protobuffer.PB;

import com.google.protobuf.Message;

import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

@EqualsAndHashCode(callSuper = true)
@ToString
@Setter
@Getter
@Slf4j
public final class PromptPayAccountPayload extends PaymentAccountPayload {
    private String promptPayId = "";

    public PromptPayAccountPayload(String paymentMethod, String id) {
        super(paymentMethod, id);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // PROTO BUFFER
    ///////////////////////////////////////////////////////////////////////////////////////////

    private PromptPayAccountPayload(String paymentMethod, String id,
                                    String promptPayId,
                                    long maxTradePeriod,
                                    @Nullable Map<String, String> excludeFromJsonDataMap) {
        super(paymentMethod,
                id,
                maxTradePeriod,
                excludeFromJsonDataMap);

        this.promptPayId = promptPayId;
    }

    @Override
    public Message toProtoMessage() {
        return getPaymentAccountPayloadBuilder()
                .setPromptPayAccountPayload(PB.PromptPayAccountPayload.newBuilder()
                        .setPromptPayId(promptPayId))
                .build();
    }

    public static PromptPayAccountPayload fromProto(PB.PaymentAccountPayload proto) {
        return new PromptPayAccountPayload(proto.getPaymentMethodId(),
                proto.getId(),
                proto.getPromptPayAccountPayload().getPromptPayId(),
                proto.getMaxTradePeriod(),
                CollectionUtils.isEmpty(proto.getExcludeFromJsonDataMap()) ? null : new HashMap<>(proto.getExcludeFromJsonDataMap()));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getPaymentDetails() {
        return "PromptPay ID: " + promptPayId;
    }

    @Override
    public String getPaymentDetailsForTradePopup() {
        return getPaymentDetails();
    }

    @Override
    public byte[] getAgeWitnessInputData() {
        return super.getAgeWitnessInputData(promptPayId.getBytes(Charset.forName("UTF-8")));
    }
}
