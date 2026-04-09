CREATE TABLE public.parada (
                               id serial4 NOT NULL,
                               nombre varchar(100) NULL,
                               tipo varchar(50) NULL,
                               CONSTRAINT parada_pkey PRIMARY KEY (id)
);


CREATE TABLE public.ruta (
                             id serial4 NOT NULL,
                             nombre varchar(100) NULL,
                             id_origen int4 NULL,
                             id_destino int4 NULL,
                             distancia float8 NULL,
                             costo_base float8 NULL,
                             tiempo_base float8 NULL,
                             transbordos int4 DEFAULT 0 NULL,
                             CONSTRAINT ruta_pkey PRIMARY KEY (id)
);


ALTER TABLE public.ruta ADD CONSTRAINT ruta_id_destino_fkey FOREIGN KEY (id_destino) REFERENCES public.parada(id) ON DELETE CASCADE;
ALTER TABLE public.ruta ADD CONSTRAINT ruta_id_origen_fkey FOREIGN KEY (id_origen) REFERENCES public.parada(id) ON DELETE CASCADE;