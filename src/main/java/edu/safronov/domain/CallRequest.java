package edu.safronov.domain;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CallRequest implements Comparable<CallRequest>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private LocalDateTime date;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private boolean active;

    @Override
    public String toString() {
        return "CallRequest{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                ", active=" + active +
                '}';
    }

    public int compareTo(@NotNull CallRequest o) {
        if (o.getDate().isAfter(this.getDate())) {
            return -1;
        } else if (o.getDate().isAfter(this.getDate())) {
            return 1;
        }
        return 0;
    }
}
