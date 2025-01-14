import { useState, forwardRef } from 'react';

import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { ScrollView } from 'react-native-web';
import PartidaView from './PartidaView';
import JugadorView from './JugadorView';
import { IoMdPersonAdd } from "react-icons/io";
import { MdOutlinePersonAddDisabled } from "react-icons/md";
import tokenService from '../services/token.service';

const SolicitudList = forwardRef((props, ref) => {
    const jwt = tokenService.getLocalAccessToken();
    let jugadoresList = [];

    function handleDeleteRequest(player) {
        fetch(
            "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/jugador/isSolicitado/" + player.id,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if (JSON.parse(data).statusCode === 500) {
                    alert("No se puede eliminar amigo." + data)
                } else {
                    props.setJugadores(prevJugadores =>
                        prevJugadores.filter(p => p.id !== player.id)
                    );
                }


            })
            .catch((message) => console.log(message));

    }

    function handleAddFriend(player) {
        fetch(
            "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/jugador/isFriend/" + player.id,
            {
                method: "PATCH",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((response) => {
                if(response.status === 500){
                    alert("No se puede añadir amigo")
            }else{
                props.setJugadores(prevJugadores =>
                    prevJugadores.filter(p => p.id !== player.id)
                );
            }
            })
            .catch((message) => console.log(message));
    }

    if (props.jugadores.length > 0) {
        jugadoresList = props.jugadores.map((player) => {
            return (
                <div style={{ transition: 'background-color 0.3s ease, border 0.3s ease' }}
                    onMouseEnter={(e) => {
                        e.currentTarget.style.backgroundColor = 'rgba(255, 255, 255, 0.5)';
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.backgroundColor = 'transparent';
                    }}
                >
                    <div key={player.id} style={{ display: 'flex', alignItems: 'center' }}>
                        <JugadorView
                            jugador={player}

                        />
                        <button onClick={() => { handleAddFriend(player) }} style={{ marginLeft: 40, background: 'transparent', border: 'none', padding: 0, cursor: 'pointer' }}>
                            <IoMdPersonAdd style={{ fontSize: '50px', backfaceVisibility: 'hidden' }} />
                        </button>
                        <button onClick={() => { handleDeleteRequest(player) }} style={{ marginLeft: 40, background: 'transparent', border: 'none', padding: 0, cursor: 'pointer' }}>
                            <MdOutlinePersonAddDisabled style={{ fontSize: '50px' }} />
                        </button>
                    </div>
                </div>

            )
        });
    }
    return (

        <div style={{ overflowY: 'auto', maxHeight: '400px' }}>
            {jugadoresList}
        </div>


    )
}
)
export default SolicitudList;