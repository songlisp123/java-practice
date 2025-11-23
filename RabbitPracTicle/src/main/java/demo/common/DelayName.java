package demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum DelayName {
    DELAY_EX_CHANGE("delay_exExchange","延迟交换机"),
    DELAY_QUEUE("delay_queue","延迟队列"),
    DEAD_LETTER_EX_CHANGE("dlx_ex","死信交换机"),
    DEAD_LETTER_QUEUE("dlx_queue","死信队列"),
    DEAD_ROUT_KEY("dlx_route_key","死信路由"),
    DELAY_BINGING_KEY("delay_bing_queue","延迟队列绑定键"),
    DEAD_BINGING_KEY("dead_bing_queue","延迟队列绑定键");
    private String name;
    private String description;
}
