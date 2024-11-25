import { forwardRef } from 'react';

const listaUrlCartasVacias = [
  "http://localhost:8080/resources/images/cartas/1carta.jpg",
  "http://localhost:8080/resources/images/cartas/2cartas.jpg",
  "http://localhost:8080/resources/images/cartas/3cartas.jpg",
  "http://localhost:8080/resources/images/cartas/mazo.jpg",
  "http://localhost:8080/resources/images/cartas/SinCartas.jpg"
];

const CartasVolteadas = forwardRef((props, ref) => {
  const { cartasDispo, posicionListaCartas } = props; // Props
  return (
    <>
      {cartasDispo.map((cartasDisp, pos) => {

        //Hay que filtrar las nulas porque cuando tiras carta se pone a null en lugar de quitarse de la lista
        const cartasRestantes = cartasDisp.filter(item => item !== null).length; 
        

        //Segun las cartas que le queden al jugador se pondra una url u otra
        let urlCartaVolteadas;

        if (cartasRestantes === 3) 
          urlCartaVolteadas = listaUrlCartasVacias[2];
         else if (cartasRestantes === 2) 
          urlCartaVolteadas = listaUrlCartasVacias[1];
         else if(cartasRestantes === 1) 
          urlCartaVolteadas = listaUrlCartasVacias[0];
         else 
         urlCartaVolteadas=listaUrlCartasVacias[4];


        

        // Creacion de estilos segun cartas que queden y posicion 
        //La verdad que esto hay que refactorizarlo que te cagas pero funciona de puta madre
        const cartaDeUna = { position: 'fixed', transform: 'translateX(-50%)',width: '50px', height: '75px' };
        const cartaDeDos = { position: 'fixed', transform: 'translateX(-50%)',width: '75px', height: '75px' };
        const cartaDeTres = { position: 'fixed', transform: 'translateX(-50%)',width: '100px', height: '75px' };
        const listaEstilos = [
          { ...cartaDeUna, top: '50%', left: '10%' },
          { ...cartaDeUna, top: '50%', right: '10%' },
          { ...cartaDeUna, top: '70px', left: '50%' },
          { ...cartaDeDos, top: '50%',  left: '10%' },
          { ...cartaDeDos, top: '50%', right: '10%' },
          { ...cartaDeDos, top: '70px', left: '50%' },
          { ...cartaDeTres, top: '50%', left: '10%' },
          { ...cartaDeTres, top: '50%', right: '10%' },
          { ...cartaDeTres, top: '70px', left: '50%' },
        ];
          

        let estiloSegunPosicionYNumCartas; 
        console.log("posicionListaCartas: "+posicionListaCartas)
        console.log("posicion: "+pos)
        console.log("Cartas restantes: "+cartasRestantes)

        
        //Evaluar que estilo se pondrá en cada caso segun cartas que queden y posicion del jugador con respecto 
        //al que esté
        if (cartasRestantes === 3 ) {
          if((posicionListaCartas+pos)%2===0)
          estiloSegunPosicionYNumCartas = listaEstilos[8];
          else if((posicionListaCartas+1===pos) || (posicionListaCartas+1>cartasDispo.length) )
            estiloSegunPosicionYNumCartas = listaEstilos[7];
          else if((posicionListaCartas-1===pos) || (posicionListaCartas-1<0))
            estiloSegunPosicionYNumCartas = listaEstilos[6];
          else
            estiloSegunPosicionYNumCartas = listaEstilos[7];

          

        } else if (cartasRestantes === 2) {
          if((posicionListaCartas+pos)%2===0)
            estiloSegunPosicionYNumCartas = listaEstilos[5];
            else if((posicionListaCartas+1===pos) || (posicionListaCartas+1>cartasDispo.length) )
              estiloSegunPosicionYNumCartas = listaEstilos[4];
            else if((posicionListaCartas-1===pos) || (posicionListaCartas-1<0))
              estiloSegunPosicionYNumCartas = listaEstilos[3];
            else
              estiloSegunPosicionYNumCartas = listaEstilos[4];
        } else {
          if((posicionListaCartas+pos)%2===0)
            estiloSegunPosicionYNumCartas = listaEstilos[2];
          else if((posicionListaCartas+1===pos) || (posicionListaCartas+1>cartasDispo.length) )
            estiloSegunPosicionYNumCartas = listaEstilos[1];
            else if((posicionListaCartas-1===pos) || (posicionListaCartas-1<0))
              estiloSegunPosicionYNumCartas = listaEstilos[0];
            else
              estiloSegunPosicionYNumCartas = listaEstilos[1];
        }
        


        //Si la posicion es la misma que la del jugador pasado como prop no se renderiza ninguna carta volteada
        return (
          pos !== posicionListaCartas && (
            <img
              key={pos}
              src={urlCartaVolteadas}
              alt={`Quedan ${cartasRestantes}`}
              style={{margin: '5px', ...estiloSegunPosicionYNumCartas }}
            />
          )
        );
      })}
    </>
  );
});

export default CartasVolteadas;
