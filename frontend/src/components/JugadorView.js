import { useState, forwardRef, useEffect } from 'react';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import tokenService from 'frontend/src/services/token.service.js';

const user = tokenService.getUser();
const jwt = tokenService.getLocalAccessToken();

const JugadorView = forwardRef((props, ref) => {
    const [jugador, setJugador] = useState(props.jugador)
    const [friendBool, setFriendBool] = useState(props.isFriend)
    const [solicitudBool, setSolicitudBool] = useState(props.isSolicitud)

    function handleSubmit() {
        fetch(
            "/api/v1/jugador/" + user.id + "/isSolicitado/" + jugador.id,
            {
                method: "PATCH"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if(JSON.parse(data).statusCode===500){
                    alert("No se puede mandar esa solicitud")
                }else{
                    setSolicitudBool(true);
                }
                

            })
            .catch((message) => alert(message));
    }


    useEffect(() => {
        if (friendBool !== true && solicitudBool) {
            fetch(
                "/api/v1/jugador/" + user.id + "/isSolicitado/" + jugador.userName,
                {
                    method: "GET"
                }
            )
                .then((response) => response.text())
                .then((data) => {
                
                    setSolicitudBool(JSON.parse(data))
                })
                .catch((message) => alert(message));
        }
    }, [friendBool, jugador.userName, props.isFriend,solicitudBool, props.isSolicitud]);

    return (
        <div style={{ cursor: 'pointer' }}>
            <div style={{ display: 'flex', alignItems: 'center', marginLeft: 5 }}>
                <img style={{ height: 80, width: 80, borderRadius: 500 }} src={jugador.photo} alt='Foto de perfil del usuario'></img>

                <div style={{ display: '' }}>
                    <p style={{ marginLeft: 10, fontSize: 25, marginBottom: 0 }}>{jugador.firstName}</p>
                    <p style={{ marginLeft: 10, fontSize: 12, marginBottom: 0 }}>{jugador.userName}</p>

                    {friendBool &&
                        <p style={{ marginLeft: 10, color: 'rgb(96,96,96)', whiteSpace: "nowrap" }}> Último mensaje </p>
                    }


                </div>
                {!friendBool && !solicitudBool &&
                    <button class="button" style={{ margin: 10, color: 'darkgreen' }} onClick={() => handleSubmit()}>
                        Solicitud amistad
                    </button>
                }


            </div>
        </div>

    )
});

export default JugadorView;