


const listaLinksCartasVacias=["http://localhost:8080/resources/images/cartas/1carta.jpg","http://localhost:8080/resources/images/cartas/2carta.jpg","http://localhost:8080/resources/images/cartas/3carta.jpg","http://localhost:8080/resources/images/cartas/mazo.jpg"]
export default function renderCartasVolteadas(cartasDispo,posicion){ //Revisar posiciones de
        
        return (
            <>
            <img
                src={listaLinksCartasVacias[1]} 
                alt={`Carta ${0}`}
                style={{width: '50px', height: '75px', margin: '5px' }} 
                onError={(e) => (e.target.style.display = 'none')}
            />
            {console.log(cartasDispo)}
            </>
        );
}

