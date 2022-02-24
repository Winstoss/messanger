import { useParams } from "react-router";
import { useEffect, useState } from "react";
//type: MessageTypeDefiner(text, file), Accept: "application/json"

export function MessageTypeDefiner(text, file){
  if(text, !file) return "text"
  if(!text && file) return "file"
  if(text && file) return "described"
}

export function MessagePanel({ isEditor, messageId, text: messageText, file: messageFile, editing}) {
  const { type, chatId } = useParams();
  const jwt = localStorage.getItem("token");
  const [text, setText] = useState("");
  const [file, setFile] = useState("");
  var httpResponse = "";

  useEffect(()=>
  {if(isEditor){
    setText(messageText)
    setFile(messageFile)}
  }, [])
  

  async function handleSubmit(event) {

    event.preventDefault()
    console.log(text)
    console.log(file)
    console.log(event.target)
    console.log(MessageTypeDefiner(text, file))

    if(isEditor){
      const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/${messageId}`, {
        method: "PATCH",
        headers: { authorization: "Bearer " + jwt,  type:  MessageTypeDefiner(text, file),},
        body: new FormData(event.target),
      });
    } else{
      const response = await fetch(`http://localhost:8080/chats/${type}/${chatId}/messages/`, {
        method: "POST",
        headers: { authorization: "Bearer " + jwt,  type:  MessageTypeDefiner(text, file),},
        body: new FormData(event.target),
      });
    }

    const res = await response.json();
    console.log(res)
    httpResponse = res;
    if(httpResponse.messageId){
        alert("message sent")
    }

  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="input here..."
          name="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <input type="file" name="file" onChange={(e) => {setFile(true)}} />
        <input type="submit" value="Send" />
        {isEditor && <button value="cancel editing" onClick={()=> editing(false)}/>}
      </form>
    </div>
  );
}
