package com.example.telegram_bot.dto.superjob;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Builder
@Getter
public class SubscriptionArgs {
    private final int payment_from;
    private final int payment_to;
    private final int town;
    private final List<Catalogues> catalogues;

    public Map populateQueries() {
        Map queries = new HashMap<>();
        if(nonNull(payment_from)) {
            queries.put("payment_from", payment_from);
        }
        if(nonNull(payment_to)) {
            queries.put("payment_to", payment_to);
        }
        if(nonNull(town)) {
            queries.put("town", town);
        }
        if(nonNull(catalogues)) {
            queries.put("catalogues", catalogues);
        }
        return queries;
    }
}
