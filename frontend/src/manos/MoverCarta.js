import React, { useRef, useState } from 'react'; 

const MoverCarta = () => {

    const cartaAMover = useRef(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });

    const handleMouseDown = (e) => { 
        const element = cartaAMover.current; 
        const shiftX = e.clientX - element.getBoundingClientRect().left; 
        const shiftY = e.clientY - element.getBoundingClientRect().top; 
        
        const handleMouseMove = (e) => { 
            setPositionCarta({ x: e.clientX - shiftX, y: e.clientY - shiftY, });
        };

        const handleMouseUp = () => { 
            document.removeEventListener('mousemove', handleMouseMove); 
            document.removeEventListener('mouseup', handleMouseUp); 
        };

        document.addEventListener('mousemove', handleMouseMove); 
        document.addEventListener('mouseup', handleMouseUp); 
    };
    
    return ( 
        <div 
            ref={cartaAMover} 
            className="draggable" 
            style={{ left: `${positionCarta.x}px`, top: `${positionCarta.y}px` }} 
            onMouseDown={handleMouseDown}> Arrástrame </div> );
};

export default MoverCarta;











