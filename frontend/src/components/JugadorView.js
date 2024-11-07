import { useState, forwardRef } from 'react';
import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';


const JugadorView =forwardRef((props, ref)  => {
    const [jugador,setJugador] = useState(props.jugador)


    return (
        <div style={{cursor:'pointer'}}>
        <div style={{ display: 'flex', alignItems: 'center',marginLeft:5}}>
                <img style={{ height: 80, width: 80, borderRadius: 500 }} src={jugador.photo} alt='Foto de perfil del usuario'></img>
                
                <div style={{display:''}}>
                <p style={{ marginLeft: 10, fontSize:25, marginBottom:0 }}>{jugador.firstName}</p>
                <p style={{ marginLeft: 10, fontSize:12, marginBottom:0 }}>{jugador.userName}</p>
                <p style={{ marginLeft: 10,color: 'rgb(96,96,96)' ,whiteSpace: "nowrap" }}> Último mensaje </p> 
                </div>
            </div> 
            </div>
            
    )
});

export default JugadorView;