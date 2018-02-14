package ru.javaops.masterjava.service.mail.model;

import lombok.*;
import com.bertoncelj.jdbi.entitymapper.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SentMail extends BaseEntity {
    @Column("from_mail")
    private @NonNull String from_mail;
    @Column("to_mail")
    private @NonNull String to_mail;
    @Column("result")
    private @NonNull Boolean result;

    public SentMail(String from_mail, String to_mail, Boolean result) {
        this.from_mail = from_mail;
        this.to_mail = to_mail;
        this.result = result;
    }
}
