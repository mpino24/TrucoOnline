import { forwardRef } from 'react';

const listaUrlCartasVacias = [
  "http://localhost:8080/resources/images/cartas/1carta.jpg",
  "http://localhost:8080/resources/images/cartas/2cartas.jpg",
  "http://localhost:8080/resources/images/cartas/3cartas.jpg",
  "http://localhost:8080/resources/images/cartas/mazo.jpg",
];

const CartasVolteadas = forwardRef((props, ref) => {
  const { cartasDispo, posicionListaCartas } = props; // Destructure props
  return (
    <>
      {cartasDispo.map((cartasDisp, pos) => {
        const cartasRestantes = cartasDisp.length;

        // Determine the URL of the card image
        let urlCartaVolteadas;
        if (cartasRestantes === 3) {
          urlCartaVolteadas = listaUrlCartasVacias[2];
        } else if (cartasRestantes === 2) {
          urlCartaVolteadas = listaUrlCartasVacias[1];
        } else {
          urlCartaVolteadas = listaUrlCartasVacias[0];
        }

        // Define styles based on position
        const styles = [
          { position: 'fixed', left: '10%', transform: 'translateX(-50%)' },
          { position: 'fixed', right: '10%', transform: 'translateX(-50%)' },
          { position: 'fixed', top: '60px', left: '50%', transform: 'translateX(-50%)' },
        ];

        const style =
          (posicionListaCartas + pos) % 2 !== 0 ? styles[2] : styles[posicionListaCartas];

        // Return the JSX for this card
        return (
          pos !== posicionListaCartas && (
            <img
              key={pos}
              src={urlCartaVolteadas}
              alt={`Quedan ${cartasRestantes}`}
              style={{ width: '50px', height: '75px', margin: '5px', ...style }}
              onError={(e) => (e.target.style.display = 'none')}
            />
          )
        );
      })}
    </>
  );
});

export default CartasVolteadas;
