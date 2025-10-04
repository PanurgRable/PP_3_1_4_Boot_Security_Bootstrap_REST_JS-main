const admin_api_url = 'admin/api';

//Получение зарегистрировавшегося пользователя
async function auth() {
    let res = await fetch(admin_api_url + '/auth');
    return await res.json();
}

//Заполнение верхней панели
async function upperPanel() {
    let user = await auth();
    document.getElementById("loggedUsername").textContent = user.username;
    let roles = "";
    user.roles.forEach(role => {
        roles += role.authority.substring(5, role.authority.length) + " ";
    })
    document.getElementById("loggedRoles").textContent = roles;
}

//Обновление панели юзера
async function refreshUserPanel() {
    const tbody = document.querySelector('#userInfoTablePlacement');

    let user = await auth();
    let roles = user.roles.map(role => role.authority.substring(5, role.authority.length));
    let rolesInTable = '';
    roles.forEach(role => { rolesInTable += `<div id="${role}">${role}</div>` });

    tbody.innerHTML = `<tr>
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.firstName}</td>
            <td class="align-middle">${user.lastName}</td>
            <td class="align-middle">${user.email}</td>
            <td class="align-middle">${user.username}</td>
            <td class="align-middle">${user.password}</td>
            <td class="align-middle">${rolesInTable}</td>
            </tr>`;
}