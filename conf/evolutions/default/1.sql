# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comments (
  id                        integer not null,
  tracks_id                 integer,
  comment                   varchar(255),
  constraint pk_comments primary key (id))
;

create table sectors (
  id                        integer not null,
  name                      varchar(255),
  first_point               varchar(255),
  end_point                 varchar(255),
  constraint pk_sectors primary key (id))
;

create table tracks (
  id                        integer not null,
  sectors_id                integer,
  constraint pk_tracks primary key (id))
;

create table trackLatLng (
  id                        integer not null,
  sectors_id                integer,
  latitude                  float,
  longitude                 float,
  constraint pk_trackLatLng primary key (id))
;


create table sectors_tracks (
  sectors_id                     integer not null,
  tracks_id                      integer not null,
  constraint pk_sectors_tracks primary key (sectors_id, tracks_id))
;
create sequence comments_seq;

create sequence sectors_seq;

create sequence tracks_seq;

create sequence trackLatLng_seq;

alter table comments add constraint fk_comments_tracks_1 foreign key (tracks_id) references tracks (id) on delete restrict on update restrict;
create index ix_comments_tracks_1 on comments (tracks_id);
alter table tracks add constraint fk_tracks_sectors_2 foreign key (sectors_id) references sectors (id) on delete restrict on update restrict;
create index ix_tracks_sectors_2 on tracks (sectors_id);
alter table trackLatLng add constraint fk_trackLatLng_sectors_3 foreign key (sectors_id) references sectors (id) on delete restrict on update restrict;
create index ix_trackLatLng_sectors_3 on trackLatLng (sectors_id);



alter table sectors_tracks add constraint fk_sectors_tracks_sectors_01 foreign key (sectors_id) references sectors (id) on delete restrict on update restrict;

alter table sectors_tracks add constraint fk_sectors_tracks_tracks_02 foreign key (tracks_id) references tracks (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comments;

drop table if exists sectors;

drop table if exists sectors_tracks;

drop table if exists tracks;

drop table if exists trackLatLng;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists comments_seq;

drop sequence if exists sectors_seq;

drop sequence if exists tracks_seq;

drop sequence if exists trackLatLng_seq;

