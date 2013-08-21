-- SCRIPT SQL CREATION INDEXES FOR WISHLIST

-- INDEX FOR wl_user
CREATE UNIQUE INDEX i_email ON wl_user USING btree (email);
CREATE UNIQUE INDEX i_username ON wl_user USING btree (username);


-- INDEX FOR wl_tr_user_authority
CREATE INDEX fki_authority_id ON wl_tr_user_authority USING btree (authority_id );
CREATE INDEX fki_user_id ON wl_tr_user_authority USING btree (user_id );