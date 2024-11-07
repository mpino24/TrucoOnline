import { forwardRef, useState } from 'react';
import { Button, Label, Form, Input } from "reactstrap";
import { IoIosSearch } from "react-icons/io";
import JugadorView from './JugadorView';
import JugadorList from "./JugadorList";

const GetFriendsModal = forwardRef((props, ref) => {
    const [player,setPlayer]= useState(null);
    const [userName,setUsername] = useState("");


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



    return (
        <div style={{ backgroundImage: 'url(/fondos/fondoAmigosModal.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100%', width: '100%'}}>

            <h1 style={{ fontSize: 30 ,position:'relative',textAlign:'center'}}>
                Chats
            </h1>
            <hr></hr>
            <div style={{ display: 'flex', alignItems: 'center' }}>
            <Form onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                    <div style={{ display:'flex', alignItems: 'center' }}>
                        <input onChange={handleChange} style={{ color: "black" }} placeholder="Buscar..." class="input" name="text" type="text" required />
                        <button style={{background: 'transparent', border:'transparent'}}>
                            <IoIosSearch style={{ width: 40, height: 40, cursor: 'pointer' }} />
                        </button>
                    </div>
                </Form>
            </div>
            {player &&
                <JugadorView jugador={player}/>}
            <div style={{overflowY:'auto'}}>
                <JugadorList/>
                </div>

        </div>




    )







}
)
export default GetFriendsModal;
