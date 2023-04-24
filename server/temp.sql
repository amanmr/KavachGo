create table contacts(
    id int primary key AUTO_INCREMENT,
    userMobile varchar(10) not null,
    contactMobile varchar(10) not null,
    foreign key(userMobile) references users(mobile)
);