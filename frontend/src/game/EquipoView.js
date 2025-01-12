import { forwardRef } from 'react';
import JugadorView from "../components/JugadorView.js";
import { CiCirclePlus } from "react-icons/ci";
import tokenService from "../services/token.service.js";
import { AiOutlineUserDelete } from "react-icons/ai";
import { FaRegHandPointRight } from "react-icons/fa6";
import { FaCrown } from "react-icons/fa";
const EquipoView = forwardRef((props, ref) => {
    const usuario = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    let jugadoresList = [];

    function changeTeam() {
        fetch(
            "/api/v1/partidajugador/changeteam?userId=" + usuario.id,
            {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                  }
            }
        )
            .then((response) => {
                if (!response.ok) {
                    alert("Error al cambiar de equipo")
                }
            })
            .catch((message) => alert(message));

    }

    function handleExpelPlayer(playerId){
        fetch(
            "/api/v1/partidajugador/eliminarJugador/"+playerId,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                  },
            }
        )
            .then((response) => response.text())
            .then((data) => {
            })
            .catch((message) => alert(message));

    }

    function getPlayerKey(player) {
        let res = player.id;
        if(player.isConnected===false){
            res+=props.jugadores.length;
        }
        return res;
    }

    function getJugadoresList() {
        if(props.jugadores.length > 0){
         jugadoresList = props.jugadores.map(j => j.player).map((player) => {
            return (
                <div key={getPlayerKey(player)} style={{ marginBottom: 5, textAlign: 'left', display: 'flex', alignItems: 'center', }}>
                    {props.gameCreator && props.gameCreator.id === usuario.id && player.id !== usuario.id &&
                        <AiOutlineUserDelete style={{ width: 40, height: 40, cursor: "pointer" }} onClick={()=> handleExpelPlayer(player.id)} />
                    }
                    {props.gameCreator && props.gameCreator.id === player.id && player.id !== usuario.id &&
                        <FaCrown style={{ width: 40, height: 40,color:'black' }} />
                    }
                    {player.id === usuario.id &&
                        <FaRegHandPointRight style={{ width: 40, height: 40, color:"black" }} />
                    }
                    <JugadorView
                        key={getPlayerKey(player)}
                        jugador={player}
                        interfaz={'partida'}
                    />
                </div>
            )
        });
    }

        const jugRestantes = Math.max(0, props.partida.numJugadores / 2 - props.jugadores.length);
        const botones = [];
        for (let i = 0; i < jugRestantes; i++) {
            botones.push(
                <div style={{
                    border: '2px solid black',
                    padding: '20px',
                    display: 'flex',
                    alignItems: 'center',
                    textAlign: 'center',
                    width: 'auto',
                    height: 'auto',
                    cursor: 'pointer'
                }}
                    onClick={() => {

                        //Comprobamos si en la lista de jugadores del equipo ya está el usuario
                        const jugador = props.jugadores.find(j => j.player.id === usuario.id);
                        if (jugador) {
                            alert("Ya perteneces a este equipo");

                        } else {
                            changeTeam();
                        }
                    }



                    }
                >
                    <CiCirclePlus style={{ width: 30, height: 30, color: 'black', marginRight: 10 }} />
                    <p style={{ margin: 0 }}>Unirse al equipo</p>
                </div>


            );
        }
        return (
            <div>
                <p>{jugadoresList}</p>
                <p>{botones}</p>
            </div>
        )

    }



    return (
        <>
            {getJugadoresList()}
        </>
    )







});
export default EquipoView;