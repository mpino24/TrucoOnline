  


public Integer sacaPuntosMasGrande(List<List<Carta>> cartasJugadores1){

List<List<Carta>> cartasEquipo1 = [];
Integer puntosEquipo1=0;
  for(List<Carta> cartasJugadores1: cartasEquipo1){
            //Map<Palo, List<Carta>> diccCartasPaloJugador = cartasEquipo1.get(i).forEach(cartasJugador -> cartasJugador.stream().collect(groupingBy(Carta::getPalo)));

             for(Carta carta: cartasJugadores1){
                Map<Palo,List<Integer>> valoresCartas=new map();
                Integer puntoJugador=0;
                Integer valorCarta= carta.getValor()<=10? 0: carta.getValor();
                if(!valoresCartas.containsKey(carta.getPalo())){
                    
                    valoresCartas.put(carta.getPalo(), new ArrayList<>(valorCarta));
                }
                else{
                    valoresCartas.put(carta.getPalo() ,valoresCartas.get(carta.getPalo()).add(valorCarta));
                    
                }
                 
                for(Palo llave:valoresCartas.keySet()){
                   List<Integer> listaValores=valoresCartas.get(llave);
                   if(listaValores.size()<=2){
                    puntoJugador= listaValores.stream().sorted(Comparator.reverseOrder()).limit(2).mapToInt(Integer::intValue).sum();
                   }
                   else{
                    if(listaValores.get(0)<puntoJugador){
                    puntoJugador=listaValores.get(0);
                    }
                   }


                }
                }

                if(puntosEquipo1>=puntoJugador){
                    puntosEquipo1=puntoJugador;
                }
            }
            return puntosEquipo1;
    }
            
