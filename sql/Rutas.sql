INSERT INTO public.ruta (nombre,id_origen,id_destino,distancia,costo_base,tiempo_base,transbordos) VALUES
	 ('AB',1,2,10.0,100.0,1.0,0),
	 ('AC',1,3,2.0,200.0,2.0,0),
	 ('AF',1,6,30.0,30.0,3.0,0),
	 ('BD',2,4,10.0,100.0,10.0,0),
	 ('BE',2,5,40.0,400.0,4.0,0),
	 ('CD',3,4,50.0,500.0,5.0,0),
	 ('CG',3,7,6.0,60.0,6.0,0),
	 ('DE',4,5,7.0,70.0,7.0,0),
	 ('DH',4,8,5.0,50.0,1.0,0),
	 ('EH',5,8,20.0,200.0,2.0,0);
INSERT INTO public.ruta (nombre,id_origen,id_destino,distancia,costo_base,tiempo_base,transbordos) VALUES
	 ('EK',5,11,2.0,20.0,2.0,0),
	 ('FI',6,9,1.0,100.0,1.0,0),
	 ('FJ',6,10,4.0,40.0,4.0,0),
	 ('GH',7,8,9.0,90.0,9.0,0),
	 ('GJ',7,10,5.0,50.0,5.0,0),
	 ('HK',8,11,11.0,11.0,1.0,0),
	 ('IJ',9,10,50.0,22.0,2.0,0),
	 ('IL',9,12,22.0,10.0,4.0,0),
	 ('JM',10,13,5.0,800.0,1.0,0),
	 ('KN',11,14,10.0,5.0,1.0,0);
INSERT INTO public.ruta (nombre,id_origen,id_destino,distancia,costo_base,tiempo_base,transbordos) VALUES
	 ('LM',12,13,3.0,30.0,1.0,0),
	 ('MN',13,14,4.0,40.0,4.0,0),
	 ('KO',11,15,1.0,200.0,2.0,0),
	 ('NO',14,15,9.0,11.0,9.0,0),
	 ('OA',15,1,3.0,30.0,1.0,0);
