-- SCRIPT SQL CREATION DB FOR WISHLIST


-- CREATE TABLE wl_user
CREATE TABLE wl_user
(
  user_id bigint NOT NULL,
  username character varying(30) NOT NULL,
  email character varying(50) NOT NULL,
  password character varying(256) NOT NULL,
  enabled boolean DEFAULT false,
  CONSTRAINT user_id PRIMARY KEY (user_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wl_user
  OWNER TO wishlist;
  
  
-- CREATE TABLE wl_authority
CREATE TABLE wl_authority
(
  authority_id bigint NOT NULL,
  authority_name character varying(10) NOT NULL,
  CONSTRAINT authority_id PRIMARY KEY (authority_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wl_authority
  OWNER TO wishlist;

  
-- CREATE TABLE wl_tr_user_authority
CREATE TABLE wl_tr_user_authority
(
  user_id bigint NOT NULL,
  authority_id bigint NOT NULL,
  CONSTRAINT pk PRIMARY KEY (user_id , authority_id ),
  CONSTRAINT fk_authority_id FOREIGN KEY (authority_id)
      REFERENCES wl_authority (authority_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_user_id FOREIGN KEY (user_id)
      REFERENCES wl_user (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wl_tr_user_authority
  OWNER TO wishlist;
  