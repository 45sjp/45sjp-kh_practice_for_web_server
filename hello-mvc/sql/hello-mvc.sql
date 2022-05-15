--=======================================================
-- 관리자 계정
--=======================================================
-- web 계정
alter session set "_oracle_script" = true; -- c##으로 시작하지 않는 일반계정생성 허용

create user web
identified by web
default tablespace users;

grant connect, resource to web;

alter user web quota unlimited on users;

--=======================================================
-- web 계정
--=======================================================
-- 회원테이블 생성
create table member (
    member_id varchar2(15),
    password varchar2(300) not null,
    member_name varchar2(50) not null,
    member_role char(1) default 'U' not null,
    gender char(1),
    birthday date,
    email varchar2(200),
    phone char(11) not null,
    address varchar2(200),
    hobby varchar2(200),
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_member_role check(member_role in ('U', 'A')),
    constraint ck_member_gender check(gender in ('M', 'F')),
    constraint uq_member_email unique(email)
);

--회원 추가
insert into member values (
    'honggd', '1234', '홍길동', 'U', 'M', to_date('20000909','yyyymmdd'),
    'honggd@naver.com', '01012341234', '서울시 강남구', '운동,등산,독서', default
);

insert into member values (
    'qwerty', '1234', '쿠어티', 'U', 'F', to_date('19900215','yyyymmdd'),
    'qwerty@naver.com', '01012341234', '서울시 송파구', '운동,등산', default
);

insert into member values (
    'admin', '1234', '관리자', 'A', 'M', to_date('19801010','yyyymmdd'),
    'admin@naver.com', '01056785678', '서울시 관악구', '게임,독서', default
);

select * from member;
commit;