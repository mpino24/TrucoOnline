import React, { createContext, useState } from 'react'; 

const CartaContext = createContext(); 
export const CartaProvider = ({ children }) => { 
    const [cartaSeleccionada, setCartaSeleccionada] = useState(null); 
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 }); 
    return ( 
        <CartaContext.Provider value={{ cartaSeleccionada, setCartaSeleccionada, positionCarta, setPositionCarta }}> 
        {children} </CartaContext.Provider> 
    ); 
};

export default CartaContext;