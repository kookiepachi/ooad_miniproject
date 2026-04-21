const apiBase = "";
const DELIVERY_FEE = 40;

function getUserId() {
    return localStorage.getItem("qc_userId");
}

function getUserEmail() {
    return localStorage.getItem("qc_userEmail");
}

function setUser(userId, email) {
    localStorage.setItem("qc_userId", userId);
    localStorage.setItem("qc_userEmail", email);
}

function clearUser() {
    localStorage.removeItem("qc_userId");
    localStorage.removeItem("qc_userEmail");
}

function formatCurrency(value) {
    return `Rs ${Number(value || 0).toFixed(0)}`;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#39;");
}

function renderHeader(cartCount = 0) {
    const header = document.getElementById("app-header");
    if (!header) return;

    const userEmail = getUserEmail();
    header.innerHTML = `
        <div class="brand"><a href="/products.html">FastFood Delivery</a></div>
        <div class="nav-links">
            <a href="/products.html">Restaurants</a>
            <a href="/cart.html">Cart${cartCount > 0 ? ` (${cartCount})` : ""}</a>
            <a href="/orders.html">Orders</a>
            <a href="/deliveries.html">Deliveries</a>
        </div>
        <div class="profile">
            ${userEmail ? `<span>Hi, ${escapeHtml(userEmail)}</span><button type="button" onclick="logout()">Logout</button>` : `<a href="/login.html">Login</a><a href="/register.html">Register</a>`}
        </div>
    `;
}

function showMessage(text, type = "info") {
    const alertBox = document.getElementById("alert-box");
    if (!alertBox) return;
    alertBox.innerHTML = `<div class="alert ${type}">${escapeHtml(text)}</div>`;
    setTimeout(() => {
        alertBox.innerHTML = "";
    }, 4000);
}

function logout() {
    clearUser();
    window.location.href = "/login.html";
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
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Registration failed");
        setUser(data.userId, data.email);
        showMessage("Registered successfully. Redirecting to products...", "success");
        setTimeout(() => {
            window.location.href = "/products.html";
        }, 1000);
    } catch (error) {
        showMessage(error.message || "Registration failed", "error");
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
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Login failed");
        if (!data.userId) throw new Error("Login response did not include a user id.");
        setUser(data.userId, data.email || payload.email);
        showMessage("Logged in successfully. Redirecting to products...", "success");
        setTimeout(() => {
            window.location.href = "/products.html";
        }, 1000);
    } catch (error) {
        showMessage(error.message || "Login failed", "error");
    }
}

async function loadProducts() {
    const container = document.getElementById("restaurants-container");
    if (!container) return;

    try {
        container.innerHTML = `<div class="empty">Loading menu...</div>`;
        const response = await fetch(`${apiBase}/products/available`);
        const products = await response.json();

        if (!response.ok) {
            throw new Error(products.message || "Unable to load products");
        }

        if (!products.length) {
            container.innerHTML = `<div class="empty">No products are available right now.</div>`;
            return;
        }

        container.innerHTML = products.map(product => `
            <article class="product-card card">
                <img class="product-image" src="${escapeHtml(product.imageUrl || "https://via.placeholder.com/600x400?text=Food")}" alt="${escapeHtml(product.productName)}">
                <div class="product-content">
                    <h3>${escapeHtml(product.productName)}</h3>
                    <p class="product-description">${escapeHtml(product.description || "Delicious food made fresh every day.")}</p>
                    <div class="product-rating-row">
                        <span class="rating">Star ${Number(product.rating || 0).toFixed(1)}</span>
                        <span>(${Number(product.reviewCount || 0)} reviews)</span>
                    </div>
                    <div class="product-meta-row">
                        <span class="product-price">${formatCurrency(product.price)}</span>
                        <span class="stock-pill">${product.stock > 0 ? `${product.stock} left` : "Out of stock"}</span>
                    </div>
                    <button ${product.stock <= 0 ? "disabled" : ""} onclick="addToCart(${product.productId})">
                        ${product.stock > 0 ? "Add to Cart" : "Out of Stock"}
                    </button>
                </div>
            </article>
        `).join("");

        await refreshHeaderCartCount();
    } catch (error) {
        container.innerHTML = `<div class="empty">${escapeHtml(error.message || "Unable to load products.")}</div>`;
    }
}

async function refreshHeaderCartCount() {
    const userId = getUserId();
    if (!userId) {
        renderHeader(0);
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}`);
        const cart = await response.json();
        if (!response.ok) throw new Error();
        const count = (cart.cartItems || []).reduce((sum, item) => sum + Number(item.quantity || 0), 0);
        renderHeader(count);
    } catch {
        renderHeader(0);
    }
}

async function addToCart(productId) {
    const userId = getUserId();
    if (!userId) {
        showMessage("Please login first to add items to cart.", "warning");
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}/add/${productId}?quantity=1`, {
            method: "POST"
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || "Failed to add item");
        showMessage("Product added to cart.", "success");
        await refreshHeaderCartCount();
    } catch (error) {
        showMessage(error.message || "Unable to add product", "error");
    }
}

function renderCartTotals(subtotal) {
    const subtotalNode = document.getElementById("cart-subtotal");
    const totalNode = document.getElementById("cart-total");
    const deliveryNode = document.getElementById("delivery-fee");
    const total = subtotal > 0 ? subtotal + DELIVERY_FEE : 0;

    if (subtotalNode) subtotalNode.textContent = formatCurrency(subtotal);
    if (deliveryNode) deliveryNode.textContent = formatCurrency(subtotal > 0 ? DELIVERY_FEE : 0);
    if (totalNode) totalNode.textContent = formatCurrency(total);
}

async function loadCart() {
    const container = document.getElementById("cart-items");
    if (!container) return;

    const userId = getUserId();
    if (!userId) {
        renderHeader(0);
        container.innerHTML = `<div class="empty">Please login to view your cart.</div>`;
        renderCartTotals(0);
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}`);
        const cart = await response.json();
        if (!response.ok) throw new Error(cart.message || "Unable to load cart");

        const items = cart.cartItems || [];
        const cartCount = items.reduce((sum, item) => sum + Number(item.quantity || 0), 0);
        renderHeader(cartCount);

        if (!items.length) {
            container.innerHTML = `<div class="empty">Your cart is empty. Add items from the restaurants page.</div>`;
            renderCartTotals(0);
            return;
        }

        container.innerHTML = items.map(item => {
            const product = item.product || {};
            return `
                <div class="cart-item">
                    <img src="${escapeHtml(product.imageUrl || "https://via.placeholder.com/240x240?text=Food")}" alt="${escapeHtml(product.productName || "Food item")}">
                    <div>
                        <h3>${escapeHtml(product.productName || "Menu Item")}</h3>
                        <p>Qty: ${Number(item.quantity || 0)}</p>
                        <p>Price: ${formatCurrency(item.totalPrice || 0)}</p>
                        <button type="button" onclick="removeCartItem(${product.productId})">Remove</button>
                    </div>
                </div>
            `;
        }).join("");

        renderCartTotals(Number(cart.totalPrice || 0));
    } catch (error) {
        container.innerHTML = `<div class="empty">${escapeHtml(error.message || "Unable to load cart.")}</div>`;
        renderCartTotals(0);
        renderHeader(0);
    }
}

async function removeCartItem(productId) {
    const userId = getUserId();
    if (!userId) {
        showMessage("Please login first.", "warning");
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}/remove/${productId}`, {
            method: "DELETE"
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || "Unable to remove item");
        showMessage("Item removed from cart.", "success");
        await loadCart();
    } catch (error) {
        showMessage(error.message || "Unable to remove item", "error");
    }
}

async function clearCart() {
    const userId = getUserId();
    if (!userId) {
        showMessage("Please login first.", "warning");
        return;
    }

    try {
        const response = await fetch(`${apiBase}/cart/${userId}/clear`, {
            method: "DELETE"
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || "Unable to clear cart");
        showMessage("Cart cleared successfully.", "success");
        await loadCart();
    } catch (error) {
        showMessage(error.message || "Unable to clear cart", "error");
    }
}

async function createOrder(event) {
    event.preventDefault();
    const userId = getUserId();
    if (!userId) {
        showMessage("Please login first to checkout.", "warning");
        return;
    }

    const form = document.getElementById("checkout-form");
    if (!form) return;

    const payload = {
        deliveryAddress: form.deliveryAddress.value,
        deliveryCity: form.deliveryCity.value,
        deliveryZipCode: form.deliveryZipCode.value,
        paymentMethod: form.paymentMethod.value,
        discountCode: form.discountCode.value,
        specialInstructions: form.specialInstructions.value
    };

    try {
        const response = await fetch(`${apiBase}/orders/create?userId=${userId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const text = await response.text();
        if (!response.ok) throw new Error(text || "Order creation failed");
        showMessage(text || "Order placed successfully.", "success");
        setTimeout(() => {
            window.location.href = "/orders.html";
        }, 1100);
    } catch (error) {
        showMessage(error.message || "Unable to place order", "error");
    }
}

async function loadOrders() {
    const container = document.getElementById("order-history");
    if (!container) return;

    const userId = getUserId();
    await refreshHeaderCartCount();

    if (!userId) {
        container.innerHTML = `<div class="empty">Please login to view your order history.</div>`;
        return;
    }

    try {
        const response = await fetch(`${apiBase}/orders/history/${userId}`);
        const orders = await response.json();
        if (!response.ok) throw new Error(orders.message || "Unable to load orders");

        if (!orders.length) {
            container.innerHTML = `<div class="empty">No orders yet. Place your first order from the cart page.</div>`;
            return;
        }

        container.innerHTML = orders.map(order => `
            <div class="order-card">
                <h3>Order #${order.orderId}</h3>
                <p><span class="status-badge">${escapeHtml(order.orderStatus || "Processing")}</span></p>
                <p><strong>Total:</strong> ${formatCurrency(order.totalAmount)}</p>
                <p><strong>Payment:</strong> ${escapeHtml(order.paymentMethod || "N/A")}</p>
                <p><strong>Delivery:</strong> ${escapeHtml(order.deliveryAddress || "N/A")}, ${escapeHtml(order.deliveryCity || "")} ${escapeHtml(order.deliveryZipCode || "")}</p>
                <p><strong>Placed:</strong> ${order.createdAt ? new Date(order.createdAt).toLocaleString() : "N/A"}</p>
                <p><strong>Items:</strong> ${Array.isArray(order.items) ? order.items.length : 0}</p>
            </div>
        `).join("");
    } catch (error) {
        container.innerHTML = `<div class="empty">${escapeHtml(error.message || "Unable to load orders.")}</div>`;
    }
}

async function loadDeliveries() {
    const container = document.getElementById("deliveries-container");
    if (!container) return;

    const userId = getUserId();
    await refreshHeaderCartCount();

    if (!userId) {
        container.innerHTML = `<div class="delivery-card"><p>Please login to view delivery updates.</p></div>`;
        return;
    }

    try {
        const response = await fetch(`${apiBase}/orders/history/${userId}`);
        const orders = await response.json();
        if (!response.ok) throw new Error(orders.message || "Unable to load deliveries");

        if (!orders.length) {
            container.innerHTML = `<div class="delivery-card"><p>No deliveries yet. Place an order to start tracking it here.</p></div>`;
            return;
        }

        container.innerHTML = orders.map(order => `
            <div class="delivery-card">
                <h3>Delivery for Order #${order.orderId}</h3>
                <p><span class="status-badge">${escapeHtml(order.orderStatus || "Processing")}</span></p>
                <p><strong>Destination:</strong> ${escapeHtml(order.deliveryAddress || "N/A")}, ${escapeHtml(order.deliveryCity || "")}</p>
                <p><strong>Tracking:</strong> ${escapeHtml(order.trackingNumber || "Tracking pending")}</p>
                <p><strong>Estimated Window:</strong> 25-35 minutes</p>
            </div>
        `).join("");
    } catch (error) {
        container.innerHTML = `<div class="delivery-card"><p>${escapeHtml(error.message || "Unable to load deliveries.")}</p></div>`;
    }
}

window.appInit = () => {
    renderHeader(0);

    const registerForm = document.getElementById("register-form");
    if (registerForm) registerForm.addEventListener("submit", registerUser);

    const loginForm = document.getElementById("login-form");
    if (loginForm) loginForm.addEventListener("submit", loginUser);

    const checkoutForm = document.getElementById("checkout-form");
    if (checkoutForm) checkoutForm.addEventListener("submit", createOrder);

    if (document.getElementById("restaurants-container")) loadProducts();
    if (document.getElementById("cart-items")) loadCart();
    if (document.getElementById("order-history")) loadOrders();
    if (document.getElementById("deliveries-container")) loadDeliveries();
};
