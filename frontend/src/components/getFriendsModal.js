import { forwardRef, useState } from 'react';
import { Button, Label, Form, Input } from "reactstrap";
import { IoIosSearch } from "react-icons/io";
import JugadorView from './JugadorView';
import JugadorList from "./JugadorList";
import { IoCloseCircle } from "react-icons/io5";
import tokenService from 'frontend/src/services/token.service.js';
import useFetchState from "frontend/src/util/useFetchState.js";

const GetFriendsModal = forwardRef((props, ref) => {
    const [player,setPlayer]= useState(null);
    const [userName,setUsername] = useState("");
    const user = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [amigos, setAmigos] = useFetchState(
        [],
        `/api/v1/jugador/amigos?userId=`+user.id,
        jwt,
        setMessage,
        setVisible
    );

    const [friendBool,setFriendBool] = useState(false);


    function handleSubmit(event) {
        setPlayer(null)

        event.preventDefault();
        fetch(
            "/api/v1/jugador/"+userName,
            {
                method: "GET"
            }
        )
            .then((response) =>  response.text())
            .then((data) => {
                
                if(data.includes('"statusCode":40')){
                    alert('Jugador ' + userName + ' no encontrado')
                }else{
                    setPlayer(JSON.parse(data))
                    }                    
            })
            .catch((message) => alert(message + userName));
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setUsername(value)
    }

    function handleReset(){
        setPlayer(null);
        document.getElementById('inputId').value='';
    }



    return (
        <div style={{ backgroundImage: 'url(/fondos/fondoAmigosModal.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100%', width: '100%'}}>

            <h1 style={{ fontSize: 30 ,position:'relative',textAlign:'center'}}>
                Chats
            </h1>
            <hr></hr>
            <div style={{ display: 'flex', alignItems: 'center' }}>
            <Form onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                    <div style={{ display:'flex', alignItems: 'center' }}>
                        <input onChange={handleChange} style={{ color: "black" }} id='inputId' placeholder="Buscar..." class="input" name="text" type="text" required />
                        <div>
                        <button style={{background: 'transparent', border:'transparent'}}>
                            <IoIosSearch style={{ width: 40, height: 40, cursor: 'pointer' }} />
                            
                        </button>
                        <IoCloseCircle style={{ width: 40, height: 40, cursor: "pointer", position:'absolute', marginLeft:20 }} onClick={() => handleReset()} />
                        </div>
                    </div>
                </Form>
            </div>
            {player &&
                <JugadorView jugador={player}/>}
            <div style={{overflowY:'auto'}}>
            {
                !player &&
                <JugadorList jugadores={amigos}/>
            }   
                
                </div>

        </div>




    )







}
)
export default GetFriendsModal;
