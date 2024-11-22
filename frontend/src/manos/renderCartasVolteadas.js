


const listaUrlCartasVacias=["http://localhost:8080/resources/images/cartas/1carta.jpg","http://localhost:8080/resources/images/cartas/2carta.jpg","http://localhost:8080/resources/images/cartas/3carta.jpg","http://localhost:8080/resources/images/cartas/mazo.jpg"]
export default function renderCartasVolteadas(cartasDispo,posicion){ //Revisar posiciones de
        
    return (
        cartasDispo.map((lsitaCartas, posicionListaCartas) => {
          const cartasRestantes = lsitaCartas.length;
      
          // Determinamos que cartas quedan y en base a ellas tomamos una foto u otra
          let urlCartaVolteadas;
          if (cartasRestantes === 3) {
            urlCartaVolteadas = listaUrlCartasVacias[2];
          } else if (cartasRestantes === 2) {
            urlCartaVolteadas = listaUrlCartasVacias[1];
          } else {
            urlCartaVolteadas = listaUrlCartasVacias[0];
          }
      
          // Determinamos los estilos (que se corresponderá a la posicion de la pantalla) 
         
          const styles = [
            { position: 'absolute', left: '0px' },
            { position: 'absolute', right: '0x' },
            { position: 'absolute', top: '0px' },
            // Se pueden añadir mas en el .css
          ];
      
          // Determinamos que estilo se usará segun la posicion de la carta
          const style = styles[posicionListaCartas];
          console.log(urlCartaVolteadas)
          return (
            <img
              key={posicionListaCartas}
              src={urlCartaVolteadas}
              alt={`Quedan ${cartasRestantes}`}
              style={{ width: '50px', height: '75px', margin: '5px', ...style}}
              onError={(e) => (e.target.style.display = 'none')}
            />
          );
        })
      );
}

