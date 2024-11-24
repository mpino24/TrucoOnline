import { useState, forwardRef, useEffect, useRef } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador,setCartasJugador] = useState([])
    const [mano, setMano] = useState(null)


    const [draggedCarta, setDraggedCarta] = useState(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });

    const [tirarTrigger, setTirarTrigger] = useState(0);


    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
      );
     function fetchMano() {
            
        fetch(
            '/api/v1/manos/'+game.codigo,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                  },
            }
        )
            .then((response) => response.json())
            .then((data) => {
                setMano(data);
                let cartasActuales = data.cartasDisp[posicion]
                if(cartasJugador!==cartasActuales){
                    setCartasJugador(cartasActuales)
                }
            })
    }
    
    useEffect(() => {
        let intervalId;

        
        fetchMano();

        intervalId = setInterval(fetchMano, 1000);
        
        return () => clearInterval(intervalId)
    },[game.codigo, posicion])

    useEffect(() => {
        fetchMano();
    }, [tirarTrigger]);


    const renderCartasJugador = () => {
        if (mano && mano.cartasDisp && posicion !== null) {
            const cartasJugador = mano.cartasDisp[posicion]; 

            if (!cartasJugador) {
                return <div>No hay cartas para mostrar.</div>;
            }
            return cartasJugador.map((carta, index) => (<>
                {carta &&<img
                    key={index}
                    src={carta.foto} 
                    alt={`Carta ${index + 1}`}
                    style={{ width: '50px', height: '75px', margin: '5px' }} 
                    onError={(e) => (e.target.style.display = 'none')} 
                    draggable 
                    onDragStart={(evento) => dragStart(evento, carta)} 
                    onDrag={(evento) => onDrag(evento)} 
                    onDragEnd={(evento) => onDragEnd(evento)} 
                />}
                </>
            ));
        }

        return <div>Cargando cartas...</div>;
    };


    const renderCartasMesa = () => {
        if (mano && mano.cartasLanzadasRonda) {
            const cartasLanzadas = mano.cartasLanzadasRonda; 

            if (!cartasLanzadas) {
                return <div>No hay cartas para mostrar.</div>;
            }

            return cartasLanzadas.map((carta, index) => (<>
                {carta &&
                    <img
                    key={index}
                    src={carta.foto} 
                    alt={`Carta ${index + 1}`}
                    style={{ width: '50px', height: '75px', margin: '5px' }} 
                    onError={(e) => (e.target.style.display = 'none')}  
                />}</> 
            ));
        }

        return <div>Cargando cartas lanzadas...</div>;
    };

    const dragStart = (evento, carta) => {
        setDraggedCarta(carta);
        tirarCarta(carta.id)
        evento.dataTransfer.effectAllowed ='move';
        
    }
    const onDrag = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY })};
    const onDragEnd = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY }) };

    const handleDrop = (evento) => {
        evento.preventDefault();
        // Aquí puedes manejar el caso de soltar la carta
        const offsetX = evento.clientX;
        const offsetY = evento.clientY;
        setPositionCarta({ x: offsetX, y: offsetY });
        
    };
    const allowDrop = (evento) => {
        evento.preventDefault();
    };

    


     function tirarCarta(cartaId) {
        
            fetch(`/api/v1/manos/${game.codigo}/tirarCarta/${cartaId}`, {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
            }).then((response) => response.text())
            .then((data) => {
                if(data){
                    console.log(data)
                    setTirarTrigger((prev) => prev + 1);
                }
            })
            .catch((error) => alert(error.message));

          
    }

    return (<>
            <div>
                <h3>Cartas del Jugador {Number(posicion)}</h3>

                {renderCartasJugador()} 
                {draggedCarta && 
                  <img
                  src={draggedCarta.foto} 
                  alt='Carta arrastrada'
                  style={{ position: 'absolute', left: `${positionCarta.x}px`, top: `${positionCarta.y}px`, width: '50px', height: '75px' }}
                  onError={(e) => (e.target.style.display = 'none')}
                  />  }
            </div>
            <div style={{left: "20px", position: "absolute"}}>
                {renderCartasMesa()}
            </div>
            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno && 
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "20px", position: "absolute", top: "200px"}}> 
                {cartasJugador[0]!== null && <button onClick={()=> tirarCarta(cartasJugador[0].id)} >Tirar {cartasJugador[0].valor} de {cartasJugador[0].palo}</button>}
                {cartasJugador[1] !== null&& <button onClick={()=> tirarCarta(cartasJugador[1].id)} > Tirar {cartasJugador[1].valor} de {cartasJugador[1].palo}</button>}
                {cartasJugador[2] !==null&& <button onClick={()=> tirarCarta(cartasJugador[2].id)} >Tirar {cartasJugador[2].valor} de {cartasJugador[2].palo}</button>}
            </div>}
        {mano && console.log(mano)}
        
        {console.log(posicion)}
        </>
    )
        
    

});
export default PlayingModal;