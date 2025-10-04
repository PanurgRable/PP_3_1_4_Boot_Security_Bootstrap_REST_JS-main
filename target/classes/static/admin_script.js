
refreshAdminPanel();
prepareUserRoles();
EditModal();
DeleteModal();
newUser();

//Настройка формы
function setupSomeForm(button, forDelete = false) {
    const tr = button.parentNode.parentNode;
    with (document.querySelector('#someId')) {
        value = tr.children[0].innerHTML;
        disabled = forDelete;
    }
    with (document.querySelector('#someFirstName')) {
        value = tr.children[1].innerHTML;
        disabled = forDelete;
    };
    with (document.querySelector('#someLastName')) {
        value = tr.children[2].innerHTML;
        disabled = forDelete;
    };
    with (document.querySelector('#someEmail')) {
        value = tr.children[3].innerHTML;
        disabled = forDelete;
    };
    with (document.querySelector('#someUsername')) {
        value = tr.children[4].innerHTML;
        disabled = forDelete;
    };
    with (document.querySelector('#somePassword')) {
        value = tr.children[5].innerHTML;
        disabled = forDelete;
    }
    with (document.querySelector('#someRolesPlace')) {
        selectRoles(children, tr.children[6].innerHTML);
        disabled = forDelete;
    }
    document.querySelector('#someEditSubmit').hidden = forDelete;
    document.querySelector('#someReset').hidden = forDelete;
    document.querySelector('#someDeleteSubmit').hidden = !forDelete;
}

//Нажатие на кнопку Edit в таблице пользователей
function onEditButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        setupSomeForm(button);
        document.querySelector('#someModalLabel').textContent = 'Изменить пользователя';
        document.querySelector('#someForm').ariaModal = 'show';
    })
}

//Нажатие на кнопку Delete в таблице пользователей
function onDeleteButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        setupSomeForm(button, true);
        document.querySelector('#someModalLabel').textContent = 'Удалить пользователя';
        document.querySelector('#someForm').ariaModal = 'show';
    })
}

//Подготовка перечня ролей на формах
async function prepareUserRoles() {
    document.querySelector('#someRolesPlace').innerHTML = "";
    document.querySelector('#newRolesPlace').innerHTML = "";
    let roles = await fetch(admin_api_url + '/roles');
    roles = await roles.json();
    roles.forEach(role => {
        let option = document.createElement("option");
        option.value = role.id;
        option.text = role.authority.substring(5, role.authority.length);
        option.id = option.text;
        document.querySelector('#someRolesPlace').appendChild(option);
        option = document.createElement("option");
        option.value = role.id;
        option.text = role.authority.substring(5, role.authority.length);
        option.id = option.text;
        document.querySelector('#newRolesPlace').appendChild(option);
    });
}

//Модальное окно Edit
async function EditModal() {
    document.querySelector('#someEditSubmit').addEventListener('click', async(e) => {
        e.preventDefault();
        const url = admin_api_url + `/users/${document.querySelector('#someId').value}`;
        const requestBody = JSON.stringify({
            id: document.querySelector('#someId').value,
            firstName: document.querySelector('#someFirstName').value,
            lastName: document.querySelector('#someLastName').value,
            email: document.querySelector('#someEmail').value,
            username: document.querySelector('#someUsername').value,
            password: document.querySelector('#somePassword').value,
            roles: listOfRoles(document.querySelector('#someRolesPlace').children)
        });
        await fetch(url, {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: requestBody
        });
        await refreshAdminPanel();
        document.querySelector('#someForm').reset();
    });
}

//Модальное окно Delete
async function DeleteModal() {
    document.querySelector('#someDeleteSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        let url = admin_api_url + `/users/${document.querySelector('#someId').value}`;
        await fetch(url, {
            method: "DELETE"
        });
        await refreshAdminPanel();
        document.querySelector('#someForm').reset();
    });
}

//Вкладка нового пользователя
async function newUser() {
    document.querySelector('#newUserForm').addEventListener('submit', async(e) => {
        e.preventDefault();
        const url = admin_api_url + '/users';
        const requestBody = JSON.stringify({
            firstName: document.querySelector('#newFirstName').value,
            lastName: document.querySelector('#newLastName').value,
            email: document.querySelector('#newEmail').value,
            username: document.querySelector('#newUsername').value,
            password: document.querySelector('#newPassword').value,
            roles: listOfRoles(document.querySelector('#newRolesPlace').children)
        });
        let response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: requestBody
        });
        if (response.status === 406) {
            document.querySelector('#warning').classList.remove('d-none');
        } else {
            await refreshAdminPanel();
            document.querySelector('#nav-userlist-tab').click();
            document.querySelector('#newUserForm').reset();
        }
    })
}

//Преобразование выбранных option-ов в массив ролей
function listOfRoles(options) {
    let res = [];
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            res.push({id: options[i].value, authority: 'ROLE_' + options[i].text});
        }
    }
    return res;
}
//Отметка ролей
function selectRoles(options, listRoles) {
    for (let i = 0; i < options.length; i++) {
        options[i].selected = listRoles.search(options[i].id) >= 0;
    }
}
//Снятие отметки с ролей
function resetRoles(options) {
    for (let i = 0; i < options.length; i++) {
        options[i].selected = false;
    }
}

//Обновление списка пользователей
async function refreshAdminPanel() {
    let response = await fetch(admin_api_url + '/users');
    let users = await response.json();
    document.querySelector('#usersTablePlacement').innerHTML = '';
    users.forEach(user => {
        let table = "";
        let roles = user.roles.map(role => role.authority.substring(5, role.authority.length));
        let rolesInTable = '';
        roles.forEach(role => { rolesInTable += `<div id="${role}">${role}</div>` });
        table += `<tr id="tr${user.id}">
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.firstName}</td>
            <td class="align-middle">${user.lastName}</td>
            <td class="align-middle">${user.email}</td>
            <td class="align-middle">${user.username}</td>
            <td class="align-middle">${user.password}</td>
            <td class="align-middle">${rolesInTable}</td>
            <td class="align-middle"><button class="btn btn-primary btn-sm editBtn" data-bs-toggle="modal" data-bs-target="#someModal">Изменить</button></td>
            <td class="align-middle"><button class="btn btn-danger btn-sm deleteBtn" data-bs-toggle="modal" data-bs-target="#someModal">Удалить</button></td>
            </tr>`;
        document.querySelector('#usersTablePlacement').innerHTML += table;
    });
    document.querySelectorAll('.editBtn').forEach(btn => {
        onEditButton(btn);
    });
    document.querySelectorAll('.deleteBtn').forEach(btn => {
        onDeleteButton(btn);
    })
    await upperPanel();
    await refreshUserPanel();
}
