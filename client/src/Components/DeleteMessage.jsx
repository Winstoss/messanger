import { messageTypeDefiner } from "./MessagePanel";

export async function DeleteMessage(message){
    
    const { type, chatId } = useParams();
    const jwt = localStorage.getItem("token")

    const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/${message.id}`, {
        method: "DELETE",
        headers:{ authorization: "Bearer " + jwt,  type:  messageTypeDefiner(message.text, message.file),},
    })

    return await response.json()
}