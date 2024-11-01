export default function generateRandomCode() { //Recordemos que esta funcion, tambien generada por IA crea un codigo que se auto-genera y se asigna al abrir el modal
    const length = 5;
    const characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
    let code = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        code += characters.charAt(randomIndex);
    }
    return code;
}