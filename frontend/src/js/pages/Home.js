import '../../css/StyleLogIn.css'
import {getInputLogIn, getInputRegistration} from "../Initialization";
import {switchForm} from "../FormSwitcher";

export function Home() {
    return (
        <div>
            <div className="generalBlock" id="generalBlock">
                <label className="tab active" id="LogInTab" onClick={switchForm}>Log in</label>
                <label className="tab" id="SignUpTab" onClick={switchForm}>Sign up</label>
                <div className="tab-form active">
                    <div className="logo">
                        <img
                            className="imgLogo"
                            src="media/logo.png"
                            alt="Notification System"
                        />
                    </div>
                    <div className="registrationFields">
                        <label className="text-field__label" htmlFor="email1"
                        >Почта
                        </label>
                        <br/>
                        <span className="text-field__icon input-item"
                        ><i className="fa fa-envelope-o"></i
                        ></span>
                        <input
                            className="text-field__input"
                            placeholder="Email"
                            type="text"
                            id="email1"
                        />
                        <div id="appEmailLogIn"></div>
                        <br/>
                        <label className="text-field__label" htmlFor="password1">Пароль</label>
                        <br/>
                        <span className="text-field__icon input-item"
                        ><i className="fa fa-key"></i
                        ></span>
                        <input
                            className="text-field__input"
                            placeholder="Password"
                            type="password"
                            id="password1"
                        />
                        <div id="appPasswordLogIn"></div>
                        <br/>
                    </div>
                    <button className="btn-new btnLogIn" id="logInBtn" onClick={getInputLogIn}>
                        <span>Log in</span>
                    </button>
                    <p style={{color: "white",}}>util</p>
                </div>

                <div className="tab-form" style={{minHeight: "450px",}}>
                    <div className="logo">
                        <img
                            className="imgLogo"
                            src="media/logo.png"
                            alt="Notification System"
                        />
                    </div>
                    <div className="registrationFields">
                        <label className="text-field__label" htmlFor="username2"
                        >Имя пользователя
                        </label>
                        <br/>
                        <span className="text-field__icon input-item"
                        ><i className="fa fa-user-circle"></i
                        ></span>
                        <input
                            className="text-field__input"
                            placeholder="Username"
                            type="text"
                            id="username2"
                        />
                        <div id="appNameSignUp"></div>
                        <br/>
                        <label className="text-field__label" htmlFor="mail">Почта</label>
                        <br/>
                        <span className="text-field__icon input-item"
                        ><i className="fa fa-envelope-o"></i
                        ></span>
                        <input
                            className="text-field__input"
                            placeholder="Mail"
                            type="text"
                            id="mail"
                        />
                        <div id="appMailSignUp"></div>
                        <br/>
                        <label className="text-field__label" htmlFor="password2">Пароль</label>
                        <br/>
                        <span className="text-field__icon input-item"
                        ><i className="fa fa-key"></i
                        ></span>
                        <input
                            className="text-field__input"
                            placeholder="Password"
                            type="password"
                            id="password2"
                        />
                        <div id="appPasswordSignUp"></div>
                        <br/>
                    </div>
                    <button className="btn-new btnSignUp" id="signUpBtn" onClick={getInputRegistration}>
                        <span>Sign up</span>
                    </button>
                    <p style={{color: "white",}}>util</p>
                </div>
            </div>
        </div>
    );
}