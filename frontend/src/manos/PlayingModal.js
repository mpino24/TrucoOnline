import { useState, forwardRef, useEffect } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [cartasJugador,setCartasJugador] = useState([])
    const [mano, setMano] = useState(null)
    const [tirarTrigger, setTirarTrigger] = useState(0);
    const [trucoTrigger, setTrucoTrigger] = useState(0);

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

    useEffect(() => {
        fetchMano();
    }, [trucoTrigger]);

    const renderCartasJugador = () => {
        if (mano && mano.cartasDisp && posicion !== null) {
            const cartasJugador = mano.cartasDisp[posicion];
    
            if (!cartasJugador) {
                return <div>No hay cartas para mostrar.</div>;
            }
    
            //Creamos un contenedor donde meter las 3 cartas
            const containerStyle = {
                position: 'fixed',
                top: '80%',
                left: '50%',
                transform: 'translateX(-50%)',
                display: 'flex',
                gap: '10px' //Y con un gap establecemos el espacio entre ellas
            };
    
            const itemStyle = {
                width: '50px',
                height: '75px'
            };
    
            return (
                <div style={containerStyle}>
                    {cartasJugador.map((carta, index) => (
                        carta && (
                            <img
                                key={index}
                                src={carta.foto}
                                alt={`Carta ${index + 1}`}
                                style={itemStyle}
                                onError={(e) => (e.target.style.display = 'none')}
                            />
                        )
                    ))}
                </div>
            );
        }
    
        return <div>Cargando cartas...</div>;
    };
    
    const renderCartasMesa = () => {
        if (mano && mano.cartasLanzadasRonda) {
            const cartasLanzadas = mano.cartasLanzadasRonda;
    
            if (!cartasLanzadas || cartasLanzadas.length === 0) {
                return <div>No hay cartas para mostrar.</div>;
            }
    
            // Lo mismo que renderizar las cartas del jugador
            const containerStyle = {
                position: 'fixed',
                top: '50%',
                left: '50%',
                transform: 'translateX(-50%)',
                display: 'flex',
                gap: '10px' 
            };
    
            const itemStyle = {
                width: '50px',
                height: '75px'
            };
    
            return (
                <div style={containerStyle}>
                    {cartasLanzadas.map((carta, index) => (
                        carta && (
                            <img
                                key={index}
                                src={carta.foto}
                                alt={`Carta ${index + 1}`}
                                style={itemStyle}
                                onError={(e) => (e.target.style.display = 'none')}
                            />
                        )
                    ))}
                </div>
            );
        }
    
        return <div>Cargando cartas lanzadas...</div>;
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

    function cantarTruco(truco) {
        
        fetch(`/api/v1/manos/${game.codigo}/cantarTruco/${truco}`, {
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
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    function responderTruco(respuesta) {
        
        fetch(`/api/v1/manos/${game.codigo}/responderTruco/${respuesta}`, {
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
                setTrucoTrigger((prev) => prev + 1);
            }
        })
        .catch((error) => alert(error.message));
    }

    return (<>
            <div>
                <h3>Jugador: {Number(posicion)}</h3>
                {renderCartasJugador()} 
            </div>
            <div style={{left: "20px", position: "absolute"}}>
                {renderCartasMesa()}
            </div>
            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno && !mano.esperandoRespuesta &&
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "70%", position: "fixed",transform: 'translateX(-50%)', top: "70%"}}> 
                {cartasJugador[0]!== null && <button onClick={()=> tirarCarta(cartasJugador[0].id)} >Tirar {cartasJugador[0].valor} de {cartasJugador[0].palo}</button>}
                {cartasJugador[1] !== null&& <button onClick={()=> tirarCarta(cartasJugador[1].id)} > Tirar {cartasJugador[1].valor} de {cartasJugador[1].palo}</button>}
                {cartasJugador[2] !==null&& <button onClick={()=> tirarCarta(cartasJugador[2].id)} >Tirar {cartasJugador[2].valor} de {cartasJugador[2].palo}</button>}
            </div>}
            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno &&
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "220px", position: "absolute", top: "200px"}}> 
                {<button onClick={()=> cantarTruco("TRUCO")} >Cantar TRUCO</button>}
                {<button onClick={()=> cantarTruco("RETRUCO")} >Cantar RETRUCO</button>}
                {<button onClick={()=> cantarTruco("VALECUATRO")} >Cantar VALECUATRO</button>}
            </div>}
            {mano  &&cartasJugador && Number(posicion) === mano.jugadorTurno && mano.esperandoRespuesta &&
            <div style={{ width: '150px', height: '75px', margin: '5px', left: "420px", position: "absolute", top: "200px"}}> 
                {<button onClick={()=> responderTruco("QUIERO")} >Quiero</button>}
                {<button onClick={()=> responderTruco("NO_QUIERO")} >No quiero</button>}
                {<button onClick={()=> responderTruco("SUBIR")} >Subir la apuesta</button>}
            </div>}

             <div>
                {mano && <CartasVolteadas cartasDispo={mano.cartasDisp} posicionListaCartas={posicion} />}
            </div>
        {mano && console.log(mano)}
        
        {console.log(posicion)}
        </>
    )
        
    

});
export default PlayingModal;