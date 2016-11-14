-- Table: items

-- DROP TABLE items;

CREATE TABLE items
(
  itemid serial NOT NULL,
  itemname character varying(255) NOT NULL,
  itemamount integer NOT NULL,
  CONSTRAINT items_pkey PRIMARY KEY (itemid),
  CONSTRAINT items_item_name_key UNIQUE (itemname),
  CONSTRAINT amount_check CHECK (itemamount > 0)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE items
  OWNER TO postgres;
