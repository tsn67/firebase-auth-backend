create table secrets
(
    id       bigint auto_increment
        primary key,
    content  varchar(255) not null,
    owned_by varchar(255) not null,
    constraint secrets_user_email_fk
        foreign key (owned_by) references user (email)
            on update cascade on delete cascade
);

