const apiBase = '';

function getUserId() {
    return localStorage.getItem('qc_userId');
}

function getUserEmail() {
    return localStorage.getItem('qc_userEmail');
}

function setUser(userId, email) {
    localStorage.setItem('qc_userId', userId);
    localStorage.setItem('qc_userEmail', email);
}

function clearUser() {
    localStorage.removeItem('qc_userId');
    localStorage.removeItem('qc_userEmail');
}

function renderHeader(cartCount = 0) {
    const header = document.getElementById('app-header');
    if (!header) return;
    const userId = getUserId();
    const userEmail = getUserEmail();
    header.innerHTML = `
        <div class="brand"><a href="/">FastFood Delivery</a></div>
        <div class="nav-links">
            <a href="/products">Restaurants</a>
            <a href="/cart">Cart${cartCount > 0 ? ` (${cartCount})` : ''}</a>
            <a href="/orders">Orders</a>
            <a href="/deliveries">Deliveries</a>
        </div>
        <div class="profile">
            ${userEmail ? `<span>Hi, ${userEmail}</span> <button onclick="logout()">Logout</button>` : `<a href="/login">Login</a> <a href="/register">Register</a>`}
        </div>
    `;
}

function showMessage(text, type = 'info') {
    const alertBox = document.getElementById('alert-box');
    if (!alertBox) return;
    alertBox.innerHTML = `<div class="alert ${type}">${text}</div>`;
    setTimeout(() => { alertBox.innerHTML = ''; }, 5000);
}

function logout() {
    clearUser();
    window.location.reload();
}

async function registerUser(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        email: form.email.value,
        password: form.password.value,
        fullName: form.fullName.value,
        phoneNumber: form.phoneNumber.value,
        role: form.role.value,
        address: form.address.value,
        city: form.city.value,
        zipCode: form.zipCode.value
    };

    try {
        const response = await fetch(`${apiBase}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || JSON.stringify(data));
        setUser(data.userId, data.email);
        showMessage('Registered successfully. Redirecting to products...', 'success');
        setTimeout(() => window.location.href = '/products', 1200);
    } catch (error) {
        showMessage(error.message || 'Registration failed', 'error');
    }
}

async function loginUser(event) {
    event.preventDefault();
    const form = event.target;
    const payload = {
        email: form.email.value,
        password: form.password.value
    };

    try {
        const response = await fetch(`${apiBase}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || JSON.stringify(data));
        if (!data.userId) {
            showMessage('Login succeeded, but no userId returned. Please register first.', 'warning');
            return;
        }
        setUser(data.userId, data.email || data.user);
        showMessage('Logged in successfully. Redirecting to products...', 'success');
        setTimeout(() => window.location.href = '/products', 1200);
    } catch (error) {
        showMessage(error.message || 'Login failed', 'error');
    }
}

async function loadProducts() {
    renderHeader();
    try {
        const response = await fetch(`${apiBase}/products/available`);
        const products = await response.json();
        const container = document.getElementById('product-list');
        const userId = getUserId();
        if (!container) return;
        if (!response.ok) {
            container.innerHTML = `<div class="empty">Unable to load products.</div>`;
            return;
        }
        if (!products || products.length === 0) {
            container.innerHTML = `<div class="empty">No products available yet.</div>`;
            return;
        }
        container.innerHTML = products.map(product => `
            <div class="product-card">
                <img src="${product.imageUrl || 'https://via.placeholder.com/260x180.png?text=Image'}" alt="${product.productName}">
                <div class="product-info">
                    <h3>${product.productName}</h3>
                    <p>${product.description || 'No description available.'}</p>
                    <div class="product-meta">
                        <span>₹ ${product.price}</span>
                        <span>${product.stock > 0 ? product.stock + ' in stock' : 'Out of stock'}</span>
                    </div>
                    <button ${product.stock <= 0 ? 'disabled' : ''} onclick="addToCart(${product.productId})">Add to Cart</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        container.innerHTML = `<div class="empty">${error.message}</div>`;
    }
}

async function addToCart(productId) {
    const userId = getUserId();
    if (!userId) {
        showMessage('Please login first to add items to cart.', 'warning');
        return;
    }
    try {
        const response = await fetch(`${apiBase}/cart/${userId}/add/${productId}?quantity=1`, {
            method: 'POST'
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || 'Failed to add product');
        showMessage('Product added to cart.', 'success');
    } catch (error) {
        showMessage(error.message || 'Unable to add to cart', 'error');
    }
}

async function loadCart() {
    const container = document.getElementById('cart-items');
    const totalNode = document.getElementById('cart-total');
    const userId = getUserId();
    if (!userId) {
        renderHeader();
        if (container) container.innerHTML = `<div class="empty">Please login to view your cart.</div>`;
        if (totalNode) totalNode.textContent = '₹ 0';
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}`);
        const cart = await response.json();
        if (!response.ok) throw new Error(cart.message || JSON.stringify(cart));
        const items = cart.cartItems || [];
        renderHeader(items.reduce((count, item) => count + (item.quantity || 0), 0));
        if (items.length === 0) {
            container.innerHTML = `<div class="empty">Your cart is empty. Add items from the product list.</div>`;
            totalNode.textContent = '₹ 0';
            return;
        }
        container.innerHTML = items.map(item => {
            const product = item.product || {};
            return `
                <div class="cart-row">
                    <img src="${product.imageUrl || 'https://via.placeholder.com/120.png?text=Image'}" alt="${product.productName}">
                    <div class="cart-info">
                        <h3>${product.productName}</h3>
                        <p>Qty: ${item.quantity}</p>
                        <p>Price: ₹ ${item.totalPrice}</p>
                        <button onclick="removeCartItem(${product.productId})">Remove</button>
                    </div>
                </div>
            `;
        }).join('');
        totalNode.textContent = `₹ ${cart.totalPrice || 0}`;
    } catch (error) {
        container.innerHTML = `<div class="empty">${error.message}</div>`;
        totalNode.textContent = '₹ 0';
        renderHeader();
    }
}

async function removeCartItem(productId) {
    const userId = getUserId();
    if (!userId) {
        showMessage('Please login first.', 'warning');
        return;
    }
    try {
        const response = await fetch(`${apiBase}/cart/${userId}/remove/${productId}`, {
            method: 'DELETE'
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || 'Unable to remove item');
        showMessage('Item removed from cart.', 'success');
        loadCart();
    } catch (error) {
        showMessage(error.message || 'Unable to remove item', 'error');
    }
}

async function clearCart() {
    const userId = getUserId();
    if (!userId) {
        showMessage('Please login first.', 'warning');
        return;
    }
    try {
        const response = await fetch(`${apiBase}/cart/${userId}/clear`, {
            method: 'DELETE'
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || 'Unable to clear cart');
        showMessage('Cart cleared successfully.', 'success');
        loadCart();
    } catch (error) {
        showMessage(error.message || 'Unable to clear cart', 'error');
    }
}

async function createOrder(event) {
    event.preventDefault();
    const userId = getUserId();
    if (!userId) {
        showMessage('Please login first to checkout.', 'warning');
        return;
    }

    const form = document.getElementById('checkout-form');
    if (!form) return;

    const payload = {
        deliveryAddress: form.deliveryAddress.value,
        deliveryCity: form.deliveryCity.value,
        deliveryZipCode: form.deliveryZipCode.value,
        paymentMethod: form.paymentMethod.value,
        discountCode: form.discountCode.value,
        specialInstructions: form.specialInstructions ? form.specialInstructions.value : ''
    };

    try {
        const response = await fetch(`${apiBase}/orders/create?userId=${userId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || 'Order creation failed');
        showMessage(text, 'success');
        setTimeout(() => window.location.href = '/orders', 1200);
    } catch (error) {
        showMessage(error.message || 'Unable to place order', 'error');
    }
}

async function loadOrders() {
    const container = document.getElementById('order-history');
    const userId = getUserId();
    if (!container) return;
    if (!userId) {
        container.innerHTML = `<div class="empty">Please login to view your order history.</div>`;
        return;
    }
    try {
        const response = await fetch(`${apiBase}/orders/history/${userId}`);
        const orders = await response.json();
        if (!response.ok) throw new Error(orders.message || JSON.stringify(orders));
        if (!orders || orders.length === 0) {
            container.innerHTML = `<div class="empty">No orders yet. Place your first order from the cart page.</div>`;
            return;
        }
        container.innerHTML = orders.map(order => `
            <div class="order-card">
                <h3>Order #${order.orderId} — ${order.orderStatus}</h3>
                <p><strong>Tracking:</strong> ${order.trackingNumber || 'N/A'}</p>
                <p><strong>Total:</strong> ₹ ${order.totalAmount}</p>
                <p><strong>Delivery:</strong> ${order.deliveryAddress || 'N/A'}, ${order.deliveryCity || ''} ${order.deliveryZipCode || ''}</p>
                <p><strong>Payment:</strong> ${order.paymentMethod || 'N/A'}</p>
                <p><strong>Placed:</strong> ${order.createdAt ? new Date(order.createdAt).toLocaleString() : 'N/A'}</p>
                <p><strong>Items:</strong> ${order.items ? order.items.length : 0}</p>
            </div>
        `).join('');
    } catch (error) {
        container.innerHTML = `<div class="empty">${error.message}</div>`;
    }
}

window.appInit = () => {
    renderHeader();
    const registerForm = document.getElementById('register-form');
    if (registerForm) registerForm.addEventListener('submit', registerUser);
    const loginForm = document.getElementById('login-form');
    if (loginForm) loginForm.addEventListener('submit', loginUser);
    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) checkoutForm.addEventListener('submit', createOrder);
    if (document.getElementById('product-list')) loadProducts();
    if (document.getElementById('cart-items')) loadCart();
    if (document.getElementById('order-history')) loadOrders();
};
