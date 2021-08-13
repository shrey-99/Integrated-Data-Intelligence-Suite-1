import {useFormik} from 'formik';
import React, {Component} from 'react';
import RegisterButton from "./RegisterButton";
import {Link, useHistory} from "react-router-dom";
import {UserOutlined, LockOutlined} from '@ant-design/icons';
import {Form, Input, Button, Checkbox, Card, Divider} from 'antd';


//Validation Function
const validate = (values) => {
    const errors = {};

    //email validation
    if (!values.email) {
        errors.email = 'Required';
    } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
        errors.email = 'Invalid email address';
    }

    //firstname validation
    if (!values.firstname) {
        errors.firstname = 'Required';
    }

    //lastname validation
    if (!values.lastname) {
        errors.lastname = 'Required';

    }

    //lastname validation
    if (!values.username) {
        errors.username = 'Required';
    }

    //password validation
    if (!values.password) {
        errors.password = 'required'
    }

    //confirmed password validation
    if (values.confirmedpassword !== values.password) {
        errors.confirmedpassword = 'passwords don\'t match';
    }
    return errors;
}


const RegisterPage = () => {
    let history = useHistory();
    const formik = useFormik({
        initialValues: {
            email: '',
            lastname: '',
            username: '',
            password: '',
            firstname: '',
            confirmedpassword: '',
        },
        validate,
        onSubmit: values => {
            alert(JSON.stringify(values, null, 2));

            /*
                Some ASync Function

                Make Get Request

                Update Client if get request Unsuccesfull

                else, redirect to home page
             */

            //use this to go to another page after successful validation server-side
            history.push('/');
        },
    });

    return (
        <Card
            id={"register_card"}
            className={"loginCard"}
            title="Data Intelligence Suite"
        >
            <form onSubmit={formik.handleSubmit}>

                <Form.Item
                    name="firstname"
                    label={'First Name'}
                >
                    <Input
                        id="firstname"
                        name="firstname"
                        type="text"
                        placeholder="first name"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value={formik.values.firstname}
                        prefix={<UserOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.firstname && formik.errors.firstname ? (
                        <p>{formik.errors.firstname}</p>) : null}
                </Form.Item>

                <Form.Item
                    name="lastname"
                    label={'Last Name'}
                >
                    <Input
                        id="lastname"
                        name="lastname"
                        type="text"
                        placeholder="last name"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value={formik.values.lastname}
                        prefix={<UserOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.lastname && formik.errors.lastname ? (
                        <p>{formik.errors.lastname}</p>) : null}
                </Form.Item>

                <Form.Item
                    name="username"
                    label={'User name'}
                >
                    <Input
                        id="username"
                        name="username"
                        type="text"
                        placeholder="user name"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value={formik.values.username}
                        prefix={<UserOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.username && formik.errors.username ? (
                        <p>{formik.errors.username}</p>) : null}
                </Form.Item>

                <Form.Item
                    name="email"
                    label={'email'}
                >
                    <Input
                        id="email"
                        name="email"
                        type="email"
                        placeholder="email"
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        value={formik.values.email}
                        prefix={<UserOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.email && formik.errors.email ? (
                        <p>{formik.errors.email}</p>) : null}
                </Form.Item>

                <Form.Item
                    name="password"
                    label={'password'}
                >
                    <Input.Password
                        id="password"
                        name="password"
                        type="password"
                        placeholder="Password"
                        value={formik.values.password}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur} //When the user leaves the form
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.password && formik.errors.password ? (
                        <p>{formik.errors.password}</p>) : null}

                </Form.Item>

                <Form.Item
                    name="confirmpassword"
                    label={'Confirm password'}
                >
                    <Input.Password
                        id="confirmedpassword"
                        name="confirmedpassword"
                        type="password"
                        placeholder="rewrite the password"
                        value={formik.values.confirmedpassword}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur} //When the user leaves the form
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                    />
                    {formik.touched.confirmedpassword && formik.errors.confirmedpassword ? (
                        <p>{formik.errors.confirmedpassword}</p>) : null}

                </Form.Item>

                <Form.Item>
                    <RegisterButton/>
                </Form.Item>

                <Divider className={'or_divider'}>
                    OR
                </Divider>

                <Form.Item>
                    Already have an account?
                    <Link to={"/login"}>
                        <a className={"register_link"} href="#">login</a>
                    </Link>
                </Form.Item>

                {/*<Form.Item*/}
                {/*    className={"forgot_password_link_container"}*/}
                {/*>*/}
                {/*    <a className="forgot_password_link" href="">*/}
                {/*        Forgot passw*/}
                {/*    </a>*/}
                {/*</Form.Item>*/}
            </form>
        </Card>
    );
};


export default RegisterPage;
