import {useNavigate} from "react-router-dom";

export function UserAccountPage(){
    const nav = useNavigate();
    return(
     <div><p>Скоро здесь будет аккаунт пользователя</p>
     <button onClick={()=>nav('/')} className="btn-new btnLogIn">Back</button></div>
    )
}