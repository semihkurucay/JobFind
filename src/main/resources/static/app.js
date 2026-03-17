// ==========================================
// API YAPILANDIRMASI VE MERKEZİ İSTEK YÖNETİMİ
// ==========================================
const API_BASE_URL = 'http://localhost:8080'; // Spring Boot Portu
let currentUser = JSON.parse(localStorage.getItem('user')) || null;
let currentPage = 1;
const pageSize = 5;
const TODAY_STR = new Date().toISOString().split('T')[0];

// Backend'e istek atmak için merkezi fonksiyon (Generic Wrapper Ayıklayıcı)
async function apiCall(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = localStorage.getItem('accessToken');
    
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        
        if (response.status === 401 || response.status === 403) {
            logout();
            throw new Error("Oturum süresi doldu. Lütfen tekrar giriş yapın.");
        }

        const text = await response.text();
        if (!text) return null;

        const responseData = JSON.parse(text);

        // Hata Yönetimi
        if (!response.ok || (responseData.status && responseData.status >= 400)) {
            const errorMsg = (responseData.exception && responseData.exception.exceptionMessage) 
                             || responseData.message 
                             || responseData.error 
                             || 'Sunucu hatası oluştu.';
            throw new Error(errorMsg);
        }

        // Generic Response Ayıklama
        if (responseData && responseData.data !== undefined) {
            // Pageable dönüş standartı kontrolü
            if (responseData.data && responseData.data.content !== undefined) {
                return responseData.data.content; 
            }
            return responseData.data;
        }

        return responseData;
        
    } catch (error) {
        alert(error.message);
        throw error;
    }
}

// ==========================================
// YÖNLENDİRME VE GÜVENLİK (AUTH)
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    updateNavUI();
    loadPublicView(); 
});

function updateNavUI() {
    const navArea = document.getElementById('navAuthArea');
    if (!currentUser) {
        navArea.innerHTML = `<button class="btn btn-sm btn-outline-primary fw-bold px-3 border-2" onclick="showAuthView('login')">Giriş Yap</button>
                             <button class="btn btn-sm btn-indigo fw-bold px-3" onclick="showAuthView('register')">Kayıt Ol</button>`;
    } else {
        const displayName = currentUser.role === 'EMPLOYER' 
            ? currentUser.name 
            : `${currentUser.firstName} ${currentUser.lastName}`;

        navArea.innerHTML = `<span class="fw-semibold text-muted me-2"><i class="bi bi-person-circle"></i> ${displayName}</span>
                             <button class="btn btn-sm btn-outline-danger rounded-pill px-3" onclick="logout()">Çıkış</button>`;
    }
}

async function login(e) {
    e.preventDefault(); 
    const usernameVal = document.getElementById('loginEmail').value.trim(); 
    const passVal = document.getElementById('loginPassword').value.trim(); 
    
    try {
        const responseObj = await apiCall('/auth/login', 'POST', { username: usernameVal, password: passVal });
        
        localStorage.setItem('accessToken', responseObj.accessToken);
        if(responseObj.refreshToken) localStorage.setItem('refreshToken', responseObj.refreshToken);
        
        localStorage.setItem('user', JSON.stringify(responseObj.user));
        currentUser = responseObj.user;
        
        updateNavUI(); 
        loadPublicView(); 
    } catch (err) { 
        console.error("Giriş başarısız", err); 
    }
}

async function register(e) {
    e.preventDefault(); 
    const role = document.querySelector('input[name="regRole"]:checked').value;
    const payload = { 
        username: document.getElementById('regEmail').value.trim(), 
        password: document.getElementById('regPassword').value 
    };
    
    let endpoint = '/auth/register/seeker';
    if (role === 'SEEKER') {
        payload.firstName = document.getElementById('regFirstName').value;
        payload.lastName = document.getElementById('regLastName').value;
    } else {
        endpoint = '/auth/register/employer';
        payload.companyName = document.getElementById('regCompanyName').value;
    }

    try {
        await apiCall(endpoint, 'POST', payload);
        alert("Kayıt başarılı! Lütfen giriş yapın.");
        switchAuthTab('login');
    } catch(err) { console.error("Kayıt hatası"); }
}

function logout() { 
    localStorage.clear();
    currentUser = null; updateNavUI(); loadPublicView(); 
}

// ==========================================
// VİTRİN EKRANI (PUBLIC)
// ==========================================
async function loadPublicView() {
    if(currentUser) {
        document.getElementById('authView').classList.add('hidden'); document.getElementById('publicView').classList.add('hidden');
        if(currentUser.role === 'EMPLOYER') { document.getElementById('employerView').classList.remove('hidden'); renderEmployerDashboard(); } 
        else { document.getElementById('seekerView').classList.remove('hidden'); renderSeekerDashboard(); }
        return;
    }
    
    document.getElementById('authView').classList.add('hidden'); document.getElementById('employerView').classList.add('hidden'); document.getElementById('seekerView').classList.add('hidden');
    document.getElementById('publicView').classList.remove('hidden');
    
    try {
        const stats = await apiCall('/public/stats');
        if(stats) {
            document.getElementById('statCompanies').innerText = stats.totalCompanies || 0;
            document.getElementById('statJobs').innerText = stats.totalJobs || 0;
            document.getElementById('statApps').innerText = stats.totalApps || 0;
            document.getElementById('statApproved').innerText = stats.totalApproved || 0;
        }
    } catch(e) { console.warn("İstatistikler çekilemedi"); }
    switchPublicTab('jobs'); 
}

async function renderPublicJobsList() {
    const listDiv = document.getElementById('publicJobsList'); listDiv.innerHTML = '<div class="text-center my-3"><div class="spinner-border text-primary"></div></div>';
    try {
        const activeJobs = await apiCall('/public/jobs');
        document.getElementById('publicJobsCount').innerText = `${activeJobs.length} İlan`;
        if(activeJobs.length === 0) return listDiv.innerHTML = '<div class="alert alert-light border">Aktif ilan bulunamadı.</div>';

        let html = '';
        activeJobs.forEach(job => {
            html += `<div class="list-item d-flex justify-content-between align-items-center">
                    <div><h6 class="mb-1 fw-bold company-link" onclick="openCompanyProfileModal(${job.companyId})">${job.companyName} <span class="badge bg-light text-dark border ms-2 text-decoration-none">Kontenjan: ${job.count}</span></h6><small class="text-muted d-block mb-1">${job.description}</small><small class="text-danger fw-semibold"><i class="bi bi-clock"></i> Bitiş: ${job.endDate}</small></div>
                    <div><button class="btn btn-sm btn-indigo" onclick="handlePublicApplyClick()">Başvur</button></div></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) { listDiv.innerHTML = '<div class="alert alert-danger">İlanlar yüklenemedi.</div>'; }
}

async function renderPublicCompanies(searchTerm = '') {
    const listDiv = document.getElementById('publicCompaniesList'); listDiv.innerHTML = '<div class="text-center my-3"><div class="spinner-border text-primary"></div></div>';
    try {
        const companies = await apiCall(`/public/companies?search=${searchTerm}`);
        if(companies.length === 0) return listDiv.innerHTML = '<div class="alert alert-light border">Şirket bulunamadı.</div>';

        let html = '';
        companies.forEach(company => {
            html += `<div class="list-item d-flex justify-content-between align-items-center">
                    <div><h6 class="mb-1 fw-bold company-link" onclick="openCompanyProfileModal(${company.id})">${company.name} <span class="badge bg-warning text-dark ms-2 text-decoration-none"><i class="bi bi-star-fill"></i> ${company.avgPoint || 'Yeni'}</span></h6><small class="text-muted d-block mb-1">${company.description}</small></div>
                    <div><button class="btn btn-sm btn-outline-indigo" onclick="openCompanyProfileModal(${company.id})"><i class="bi bi-building"></i> Şirketi İncele</button></div></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) { listDiv.innerHTML = '<div class="alert alert-danger">Şirketler yüklenemedi.</div>'; }
}

function handlePublicApplyClick() { alert("İlanlara başvurabilmek için giriş yapmalı veya kayıt olmalısınız."); showAuthView('login'); }
function showAuthView(tab) { document.getElementById('publicView').classList.add('hidden'); document.getElementById('employerView').classList.add('hidden'); document.getElementById('seekerView').classList.add('hidden'); document.getElementById('authView').classList.remove('hidden'); switchAuthTab(tab); }
function switchAuthTab(tab) {
    document.getElementById('tabLogin').classList.remove('active'); document.getElementById('tabRegister').classList.remove('active'); document.getElementById('formLogin').classList.add('hidden'); document.getElementById('formRegister').classList.add('hidden');
    if(tab === 'login') { document.getElementById('tabLogin').classList.add('active'); document.getElementById('formLogin').classList.remove('hidden'); } 
    else { document.getElementById('tabRegister').classList.add('active'); document.getElementById('formRegister').classList.remove('hidden'); }
}

// ==========================================
// İŞ VEREN MANTIĞI 
// ==========================================
async function createJob() {
    const payload = { count: parseInt(document.getElementById('jobCount').value), description: document.getElementById('jobDesc').value, endDate: document.getElementById('jobEndDate').value };
    try { await apiCall('/jobs', 'POST', payload); alert("İlan Yayınlandı!"); document.querySelector('#employerNav button').click(); renderEmployerDashboard(); } catch(e) {}
}

async function deleteJob(jobId) {
    if(confirm("İlanı silmek istediğinize emin misiniz?")) {
        try { await apiCall(`/jobs/${jobId}`, 'DELETE'); alert("İlan silindi."); renderEmployerDashboard(); } catch(e) {}
    }
}

async function renderEmployerDashboard() {
    const listDiv = document.getElementById('employerJobsList'); listDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const myJobs = await apiCall('/jobs/me');
        const activeJobs = myJobs.filter(j => j.endDate >= TODAY_STR);
        const pastJobs = myJobs.filter(j => j.endDate < TODAY_STR);

        let html = `<h6 class="fw-bold text-success mb-3 border-bottom pb-2"><i class="bi bi-record-circle-fill"></i> Aktif İlanlar (${activeJobs.length})</h6>`;
        if(activeJobs.length === 0) html += '<p class="text-muted small">Aktif ilanınız bulunmuyor.</p>';
        activeJobs.forEach(job => {
            html += `<div class="list-item border-start border-4 border-success d-flex justify-content-between align-items-center">
                    <div><h6 class="mb-1 fw-bold text-primary">İlan #${job.id} <span class="badge bg-light text-dark border ms-2">Kontenjan: ${job.count}</span></h6><small class="text-muted d-block mb-1">${job.description}</small><small class="text-danger fw-semibold"><i class="bi bi-clock"></i> Bitiş: ${job.endDate}</small></div>
                    <div class="text-end"><button class="btn btn-sm btn-indigo" onclick="viewApplicants(${job.id})">Başvuruları İncele</button><button class="btn btn-sm btn-outline-danger ms-1" onclick="deleteJob(${job.id})"><i class="bi bi-trash"></i></button></div></div>`;
        });

        html += `<h6 class="fw-bold text-secondary mt-5 mb-3 border-bottom pb-2"><i class="bi bi-archive-fill"></i> Geçmiş İlanlar (${pastJobs.length})</h6>`;
        pastJobs.forEach(job => {
            html += `<div class="list-item bg-light d-flex justify-content-between align-items-center opacity-75">
                    <div><h6 class="mb-1 fw-bold text-muted">İlan #${job.id}</h6><small class="text-muted d-block mb-1">${job.description}</small><small class="text-muted fw-semibold">Süresi Doldu (${job.endDate})</small></div>
                    <div class="text-end"><button class="btn btn-sm btn-secondary" onclick="viewApplicants(${job.id})">Sonuçları Gör</button><button class="btn btn-sm btn-outline-danger ms-1" onclick="deleteJob(${job.id})"><i class="bi bi-trash"></i></button></div></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) { listDiv.innerHTML = '<div class="alert alert-danger">İlanlar yüklenemedi.</div>'; }
}

async function viewApplicants(jobId) {
    document.getElementById('emp-myJobs').classList.add('hidden'); document.getElementById('emp-applicantsView').classList.remove('hidden'); document.getElementById('applicantJobTitle').innerText = `İlan #${jobId} Başvuruları`;
    const appsContainer = document.getElementById('applicantListContainer'); appsContainer.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const apps = await apiCall(`/applications/job/${jobId}`);
        if (apps.length === 0) return appsContainer.innerHTML = '<div class="alert alert-light border text-center text-muted">Bu ilana henüz başvuru yapılmamış.</div>';
        
        const statusMapLocal = { 'PENDING': { t: 'Beklemede', c: 'warning text-dark' }, 'APPROVED': { t: 'Onaylandı', c: 'success' }, 'REJECTED': { t: 'Reddedildi', c: 'danger' } };
        let html = '';
        apps.forEach(app => {
            // GÜNCELLEME: app.status yerine app.applyType ve app.applicant yerine app.user kullanıldı
            const st = statusMapLocal[app.applyType] || { t: 'Bilinmiyor', c: 'secondary' };
            html += `<div class="list-item d-flex justify-content-between align-items-center mb-2"><div><h6 class="mb-1 fw-bold"><i class="bi bi-person me-1"></i> ${app.user.firstName} ${app.user.lastName}</h6><span class="badge bg-${st.c}">${st.t}</span></div><div class="d-flex gap-2"><button class="btn btn-sm btn-light border text-primary fw-semibold" onclick="showCvModal(${app.user.id})"><i class="bi bi-file-person"></i> CV Gör</button>${app.applyType === 'PENDING' ? `<button class="btn btn-sm btn-success" onclick="updateAppStatus(${app.id}, 'APPROVED', ${jobId})"><i class="bi bi-check-lg"></i></button><button class="btn btn-sm btn-danger" onclick="updateAppStatus(${app.id}, 'REJECTED', ${jobId})"><i class="bi bi-x-lg"></i></button>` : ``}</div></div>`;
        });
        appsContainer.innerHTML = html;
    } catch(e) { appsContainer.innerHTML = 'Hata oluştu.'; }
}
function backToJobsList() { document.getElementById('emp-applicantsView').classList.add('hidden'); document.getElementById('emp-myJobs').classList.remove('hidden'); renderEmployerDashboard(); }

async function updateAppStatus(appId, newStatus, currentJobId) {
    try { await apiCall(`/applications/${appId}/status`, 'PATCH', { status: newStatus }); viewApplicants(currentJobId); } catch(e) {}
}

async function renderEmployerCompanies(searchTerm = '') {
    const listDiv = document.getElementById('employerCompaniesList'); listDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const companies = await apiCall(`/public/companies?search=${searchTerm}`);
        const otherCompanies = companies.filter(c => c.id !== currentUser.companyId);
        if(otherCompanies.length === 0) return listDiv.innerHTML = '<div class="text-muted small">Başka şirket bulunamadı.</div>';
        let html = '';
        otherCompanies.forEach(company => {
            html += `<div class="list-item d-flex justify-content-between align-items-center"><div><h6 class="mb-1 fw-bold text-primary company-link" onclick="openCompanyProfileModal(${company.id})">${company.name} <span class="badge bg-warning text-dark ms-2 text-decoration-none"><i class="bi bi-star-fill"></i> ${company.avgPoint || 'Yeni'}</span></h6><small class="text-muted d-block mb-1">${company.description}</small></div><div><button class="btn btn-sm btn-indigo" onclick="openCompanyProfileModal(${company.id})"><i class="bi bi-building"></i> Şirketi İncele</button></div></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) {}
}

async function loadCompanyProfile() {
    try {
        const company = await apiCall(`/companies/me`); // GÜNCELLEME: /companies/{id} yerine /companies/me kullanıldı
        document.getElementById('compName').value = company.name || ''; document.getElementById('compEmpCount').value = company.employeeCount || ''; document.getElementById('compDesc').value = company.description || ''; document.getElementById('compEmail').value = currentUser.username || '';
        if(company.address) { document.getElementById('compCity').value = company.address.city || ''; document.getElementById('compDistrict').value = company.address.district || ''; document.getElementById('compNeighborhood').value = company.address.neighborhood || ''; document.getElementById('compStreet').value = company.address.street || ''; }
    } catch(e) {}
}

async function updateCompanyProfile(e) {
    e.preventDefault(); 
    const payload = {
        name: document.getElementById('compName').value, employeeCount: parseInt(document.getElementById('compEmpCount').value), description: document.getElementById('compDesc').value,
        address: { city: document.getElementById('compCity').value, district: document.getElementById('compDistrict').value, neighborhood: document.getElementById('compNeighborhood').value, street: document.getElementById('compStreet').value }
    };
    try { await apiCall('/companies/me', 'PUT', payload); alert("Şirket bilgileri güncellendi."); } catch(e) {}
}

// Sayfa numarasını tutacağımız değişken (fonksiyonun dışına, hemen üstüne ekleyebilirsin)
let currentCommentPage = 0;

async function renderCompanyComments(page = 0) {
    currentCommentPage = page;
    const cDiv = document.getElementById('companyCommentsContainer'); cDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        // 1. ADIM: "undefined" hatası riskini yok etmek için /companies/me üzerinden GARANTİ şirket bilgilerini çekiyoruz.
        const myCompany = await apiCall('/companies/me');
        
        // 2. ADIM: Aldığımız bu garanti ID ile senin yazdığın o yeni sayfalama endpoint'ine vuruyoruz!
        const comments = await apiCall(`/public/companies/${myCompany.id}/comments?page=${currentCommentPage}&size=5&sort=createdDate,desc`);
        
        // Eğer yorum yoksa boş ekran göster, varsa sayfalamaya devam et
        if(!comments || comments.length === 0) {
            if (currentCommentPage > 0) return renderCompanyComments(currentCommentPage - 1);
            return cDiv.innerHTML = '<div class="alert alert-light border text-muted">Şirketinize henüz bir değerlendirme yapılmamış.</div>';
        }
        
        const drawStars = (point) => `<span class="star-rating">${'★'.repeat(Math.round(point))}${'☆'.repeat(5 - Math.round(point))}</span>`;
        
        // Veritabanına eklediğimiz o performanslı ortalama puan sütununu kullanıyoruz
        let avg = myCompany.averagePoint || myCompany.avgPoint || 0;
        let html = `<div class="d-flex align-items-center mb-4 bg-light p-3 rounded border"><span class="fs-1 fw-bold text-warning me-3">${avg > 0 ? avg : '-'}</span><div class="d-flex flex-column"><span class="fs-4" style="line-height:1;">${avg > 0 ? drawStars(avg) : ''}</span><span class="text-muted small">Güncel Ortalama</span></div></div>`;
        
        comments.forEach(c => { 
            html += `<div class="p-3 mb-3 bg-white border rounded list-item"><div class="d-flex justify-content-between align-items-center mb-2"><strong class="text-dark"><i class="bi bi-person-circle text-muted me-2"></i>${c.userFirtLastName || c.userName || 'Anonim'}</strong><span>${drawStars(c.point)}</span></div><p class="mb-2 text-muted small">${c.comment}</p><small class="text-secondary" style="font-size:0.75rem;"><i class="bi bi-calendar me-1"></i>${c.createdDate ? c.createdDate.split('-').reverse().join('.') : '?'}</small></div>`; 
        });

        // İleri/Geri Sayfalama Butonları
        html += `<div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top"><button class="btn btn-light border btn-sm px-3 fw-bold" ${currentCommentPage === 0 ? 'disabled' : ''} onclick="renderCompanyComments(${currentCommentPage - 1})"><i class="bi bi-chevron-left"></i> Önceki</button><span class="fw-bold text-muted small">Sayfa ${currentCommentPage + 1}</span><button class="btn btn-light border btn-sm px-3 fw-bold" ${comments.length < 5 ? 'disabled' : ''} onclick="renderCompanyComments(${currentCommentPage + 1})">Sonraki <i class="bi bi-chevron-right"></i></button></div>`;
        
        cDiv.innerHTML = html;
    } catch(e) { 
        console.error("Yorum çekme hatası: ", e);
        cDiv.innerHTML = '<div class="alert alert-danger">Yorumlar yüklenemedi.</div>'; 
    }
}

// ==========================================
// İŞ ARAYAN MANTIĞI
// ==========================================
async function renderSeekerDashboard() {
    document.getElementById('clearJobFilterBtn').classList.add('hidden');
    const listDiv = document.getElementById('allJobsList'); listDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const [jobs, myApps] = await Promise.all([apiCall('/public/jobs'), apiCall('/applications/me')]); 
        const appliedIds = myApps.map(app => app.jobPosting.id);
        let html = '';
        jobs.forEach(job => {
            html += `<div class="list-item d-flex justify-content-between align-items-center"><div><h6 class="mb-1 fw-bold company-link" onclick="openCompanyProfileModal(${job.companyId})">${job.companyName} <span class="badge bg-light text-dark border ms-2 text-decoration-none">Alınacak: ${job.count}</span></h6><small class="text-muted d-block mb-1">${job.description}</small><small class="text-danger"><i class="bi bi-calendar-event"></i> Son Başvuru: ${job.endDate}</small></div>${appliedIds.includes(job.id) ? `<button class="btn btn-sm btn-secondary disabled"><i class="bi bi-check-all"></i> Başvuruldu</button>` : `<button class="btn btn-sm btn-indigo" onclick="applyForJob(${job.id})">Başvur</button>`}</div>`;
        });
        listDiv.innerHTML = html || '<div class="alert alert-light text-center border">İlan bulunamadı.</div>';
    } catch(e) {}
    renderMyApplications();
}

async function applyForJob(jobId) {
    try { await apiCall('/applications', 'POST', { jobId: jobId }); alert("Başvuru iletildi."); document.querySelector('#seekerNav button').click(); renderSeekerDashboard(); } catch(e) {}
}
async function applyForJobFromModal(jobId, companyId) { await applyForJob(jobId); openCompanyProfileModal(companyId); }

async function renderMyApplications() {
    const listDiv = document.getElementById('myApplicationsList'); listDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const myApps = await apiCall('/applications/me');
        if(myApps.length === 0) return listDiv.innerHTML = '<p class="text-muted">Henüz bir başvurun yok.</p>';
        const statusMapLocal = { 'PENDING': { t: 'Beklemede', c: 'warning text-dark' }, 'APPROVED': { t: 'Onaylandı', c: 'success' }, 'REJECTED': { t: 'Reddedildi', c: 'danger' } };
        let html = '';
        myApps.forEach(app => {
            const st = statusMapLocal[app.applyType] || { t: 'Bilinmiyor', c: 'secondary' };
            const job = app.jobPosting || {};
            const compName = job.companyName || (job.company && job.company.name) || `İlan #${job.id}`;
            const compId = job.companyId || (job.company && job.company.id);
            html += `<div class="list-item d-flex justify-content-between align-items-center"><div><h6 class="mb-1 fw-bold company-link text-primary" onclick="openCompanyProfileModal(${compId})">${compName} <span class="badge bg-light text-dark border ms-2 fw-normal text-decoration-none">Kontenjan: ${job.count || '?'}</span></h6><small class="text-muted d-block mb-1">${job.description || 'Detay belirtilmemiş'}</small><small class="text-secondary" style="font-size:0.8rem;"><i class="bi bi-calendar-plus"></i> Yayın: ${job.createdDate ? job.createdDate.split('-').reverse().join('.') : '?'} <span class="mx-1">|</span> <i class="bi bi-calendar-x text-danger"></i> Bitiş: <span class="text-danger">${job.endDate ? job.endDate.split('-').reverse().join('.') : '?'}</span></small></div><span class="badge bg-${st.c} px-3 py-2" style="font-size:0.85rem;">${st.t}</span></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) {}
}

async function renderSeekerCompanies(searchTerm = '') {
    const listDiv = document.getElementById('seekerCompaniesList'); listDiv.innerHTML = '<div class="text-center"><div class="spinner-border"></div></div>';
    try {
        const companies = await apiCall(`/public/companies?search=${searchTerm}`);
        if(companies.length === 0) return listDiv.innerHTML = '<div class="text-muted small">Şirket bulunamadı.</div>';
        let html = '';
        companies.forEach(company => {
            html += `<div class="list-item d-flex justify-content-between align-items-center"><div><h6 class="mb-1 fw-bold company-link" onclick="openCompanyProfileModal(${company.id})">${company.name} <span class="badge bg-warning text-dark ms-2 text-decoration-none"><i class="bi bi-star-fill"></i> ${company.avgPoint || 'Yeni'}</span></h6><small class="text-muted d-block mb-1">${company.description}</small></div><div><button class="btn btn-sm btn-indigo" onclick="openCompanyProfileModal(${company.id})"><i class="bi bi-building"></i> Şirketi İncele</button></div></div>`;
        });
        listDiv.innerHTML = html;
    } catch(e) {}
}

function clearSeekerJobFilter() { renderSeekerDashboard(); }

async function loadSeekerProfile() {
    try {
        const user = await apiCall('/profile/me');
        document.getElementById('seekFirstName').value = user.firstName || ''; document.getElementById('seekLastName').value = user.lastName || ''; document.getElementById('seekPhone').value = user.phone || ''; document.getElementById('seekBirthDate').value = user.birthDate || ''; document.getElementById('seekEmail').value = user.username || '';
        const addr = user.address || {}; document.getElementById('seekCity').value = addr.city || ''; document.getElementById('seekDistrict').value = addr.district || ''; document.getElementById('seekNeighborhood').value = addr.neighborhood || ''; document.getElementById('seekStreet').value = addr.street || '';
    } catch(e) {}
}

async function updateSeekerProfile(e) {
    e.preventDefault(); 
    
    const newPass = document.getElementById('seekNewPass').value;
    const newPassConfirm = document.getElementById('seekNewPassConfirm').value;
    
    if (newPass || newPassConfirm) { 
        if (newPass !== newPassConfirm) {
            alert("Hata: Girdiğiniz yeni şifreler birbiriyle uyuşmuyor!");
            return; 
        }
    }
    
    // Temel payload (İsim, soyisim, adres vs.)
    const payload = {
        firstName: document.getElementById('seekFirstName').value, 
        lastName: document.getElementById('seekLastName').value, 
        phone: document.getElementById('seekPhone').value, 
        birthDate: document.getElementById('seekBirthDate').value,
        address: { 
            city: document.getElementById('seekCity').value, 
            district: document.getElementById('seekDistrict').value, 
            neighborhood: document.getElementById('seekNeighborhood').value, 
            street: document.getElementById('seekStreet').value 
        }
    };

    // ŞİFREYİ BURADA PAYLOAD'A EKLİYORUZ
    if (newPass) {
        payload.password = newPass; // Backend'de 'password' değişkeniyle karşılıyorsan
    }

    try { 
        await apiCall('/profile/me', 'PUT', payload); 
        alert("Profil kaydedildi."); 
        
        // İşlem başarılıysa şifre kutularını temizleyelim ki ekranda kalmasın
        document.getElementById('seekNewPass').value = '';
        document.getElementById('seekNewPassConfirm').value = '';
    } catch(e) {}
}

// CV İşlemleri
async function loadSeekerCv() {
    try {
        const cv = await apiCall('/cv/me');
        document.getElementById('seekBio').value = cv.bio || '';
        renderCvLists(cv);
    } catch(e) {}
}
// GÜNCELLEME: POST yerine PUT kullanıldı
async function saveCvBio() { try { await apiCall('/cv/bio', 'PUT', { bio: document.getElementById('seekBio').value }); alert("Biyografi kaydedildi."); } catch(e) {} }

function renderCvLists(cv) {
    const tm = { 'PRIMARY_SCHOOL': 'İlkokul', 'MIDDLE_SCHOOL': 'Ortaokul', 'HIGH_SCHOOL': 'Lise', 'ASSOCIATE': 'Ön Lisans', 'BACHELOR': 'Lisans', 'MASTER': 'Yüksek Lisans' }; // Çeviri Sözlüğümüz
    const sDiv = document.getElementById('cvSchoolList'); sDiv.innerHTML = ''; (cv.schoolCv||[]).forEach((s) => sDiv.innerHTML += `<div class="d-flex justify-content-between align-items-center bg-white border p-2 mb-2 rounded small"><div><strong>${s.schoolName}</strong> - ${tm[s.schoolType] || s.schoolType || ''} - ${s.description||''} - ${s.point||''} <span class="text-muted ms-1">(${ (s.startDate||'?').split('-').reverse().join('.') } / ${ (s.endDate||'Devam Ediyor').split('-').reverse().join('.') })</span></div><button class="btn btn-sm text-danger border-0" onclick="removeCvItem('schools', ${s.id})"><i class="bi bi-trash"></i></button></div>`);
    const eDiv = document.getElementById('cvExperienceList'); eDiv.innerHTML = ''; (cv.experienceCv||[]).forEach((e) => eDiv.innerHTML += `<div class="d-flex justify-content-between align-items-center bg-white border p-2 mb-2 rounded small"><div><strong>${e.name}</strong> <span class="text-muted ms-1">(${ (e.startDate||'?').split('-').reverse().join('.') } / ${ (e.endDate||'Devam Ediyor').split('-').reverse().join('.') })</span></div><button class="btn btn-sm text-danger border-0" onclick="removeCvItem('experiences', ${e.id})"><i class="bi bi-trash"></i></button></div>`);
    const lDiv = document.getElementById('cvLanguageList'); lDiv.innerHTML = ''; (cv.languageCv||[]).forEach((l) => lDiv.innerHTML += `<span class="badge bg-light text-dark border me-2 p-2">${l.name} . ${l.level} <i class="bi bi-x-circle text-danger ms-2" style="cursor:pointer;" onclick="removeCvItem('languages', ${l.id})"></i></span>`);
}

async function addSchoolToCv() { const p = { schoolName: document.getElementById('newSchoolName').value, schoolType: document.getElementById('newSchoolType').value, description: document.getElementById('newSchoolDesc').value, point: parseFloat(document.getElementById('newSchoolPoint').value) || null, startDate: document.getElementById('newSchoolStartDate').value || null, endDate: document.getElementById('newSchoolEndDate').value || null }; try { await apiCall('/cv/schools', 'POST', p); loadSeekerCv(); document.getElementById('newSchoolName').value=''; document.getElementById('newSchoolDesc').value=''; document.getElementById('newSchoolPoint').value=''; document.getElementById('newSchoolStartDate').value=''; document.getElementById('newSchoolEndDate').value=''; } catch(e) {} }
async function addExperienceToCv() { const p = { name: document.getElementById('newExpName').value, startDate: document.getElementById('newExpStart').value, endDate: document.getElementById('newExpEnd').value }; try { await apiCall('/cv/experiences', 'POST', p); loadSeekerCv(); } catch(e) {} }
async function addLanguageToCv() { const p = { name: document.getElementById('newLangName').value, level: document.getElementById('newLangLevel').value }; try { await apiCall('/cv/languages', 'POST', p); loadSeekerCv(); } catch(e) {} }
async function removeCvItem(type, id) { try { await apiCall(`/cv/${type}/${id}`, 'DELETE'); loadSeekerCv(); } catch(e) {} }

// ==========================================
// ORTAK MODALLAR
// ==========================================
async function openCompanyProfileModal(companyId) {
    try {
        // İŞTE MİKROSERVİS MANTIĞI: 3 İsteği aynı anda, beklemeden fırlatıyoruz!
        const [company, jobs, comments] = await Promise.all([
            apiCall(`/public/companies/${companyId}`),
            apiCall(`/public/companies/${companyId}/jobs`),
            apiCall(`/public/companies/${companyId}/comments?page=0&size=5`)
        ]);

        // Şirketin ortalama puanı (Backend'den hesaplanıp gelen alanı kullanıyoruz)
        let avgPoint = company.averagePoint || company.avgPoint || 0; 
        const drawStars = (point) => `<span class="star-rating">${'★'.repeat(Math.round(point))}${'☆'.repeat(5 - Math.round(point))}</span>`;
        
        let jobsHtml = '';
        if(!jobs || jobs.length === 0) jobsHtml = '<div class="alert alert-light border text-muted small text-center">Aktif ilanı bulunmuyor.</div>';
        else {
            jobs.forEach(job => {
                let actionBtn = `<button class="btn btn-sm w-100 btn-indigo" onclick="bootstrap.Modal.getInstance(document.getElementById('sharedCompanyProfileModal')).hide(); handlePublicApplyClick();">Giriş Yaparak Başvur</button>`;
                if (currentUser) {
                    if(currentUser.role === 'SEEKER') actionBtn = `<button class="btn btn-sm w-100 btn-indigo" onclick="applyForJob(${job.id}); bootstrap.Modal.getInstance(document.getElementById('sharedCompanyProfileModal')).hide();">Hemen Başvur</button>`;
                    else actionBtn = `<span class="badge bg-light text-muted border w-100 p-2">Sadece iş arayanlar başvurabilir</span>`;
                }
                // İlan tarihlerini de senin sevdiğin noktalı formata çevirdim
                jobsHtml += `<div class="border rounded p-3 mb-3 bg-white"><div class="d-flex justify-content-between align-items-center mb-2"><strong class="text-primary">Kontenjan: ${job.count}</strong><small class="text-danger fw-semibold"><i class="bi bi-clock"></i> ${job.endDate ? job.endDate.split('-').reverse().join('.') : '?'}</small></div><p class="mb-3 small text-muted">${job.description}</p>${actionBtn}</div>`;
            });
        }

        let commentsHtml = '';
        const safeComments = comments || [];
        if(safeComments.length === 0) commentsHtml = '<div class="text-muted small">Henüz yorum yapılmamış.</div>';
        else {
            // Yorum tarihlerini de noktalı formata çevirdim
            safeComments.forEach(c => commentsHtml += `<div class="border-bottom pb-2 mb-2"><div class="d-flex justify-content-between align-items-center"><strong class="small text-dark">${c.userFirtLastName || c.userName || 'Anonim'}</strong><span class="small">${drawStars(c.point)}</span></div><p class="mb-1 small text-muted mt-1">${c.comment}</p><small class="text-muted" style="font-size:0.7rem;">${c.createdDate ? c.createdDate.split('-').reverse().join('.') : '?'}</small></div>`);
        }

        let rateBtnHtml = (currentUser && currentUser.role === 'SEEKER') ? `<button class="btn btn-outline-indigo btn-sm mt-3 w-100" onclick="bootstrap.Modal.getInstance(document.getElementById('sharedCompanyProfileModal')).hide(); setTimeout(() => openRateModal(${company.id}, '${company.name}'), 400);"><i class="bi bi-pencil-square"></i> Sen de Değerlendir</button>` : '';

        const addrObj = company.address || {};
        const fullAddress = addrObj.city ? `${addrObj.neighborhood || ''} Mah. ${addrObj.street || ''} Sok. ${addrObj.district || ''} / ${addrObj.city || ''}` : 'Adres belirtilmemiş';

        document.getElementById('scpmBody').innerHTML = `<div class="row"><div class="col-md-7 border-end pe-4"><h6 class="fw-bold text-primary mb-3"><i class="bi bi-info-circle"></i> Şirket Bilgileri</h6><p class="text-muted small mb-3">${company.description || 'Açıklama bulunmuyor.'}</p><div class="bg-light p-3 rounded border mb-4"><div class="mb-2 text-muted small"><i class="bi bi-geo-alt-fill text-danger me-2"></i><strong>Adres:</strong> ${fullAddress}</div><div class="text-muted small"><i class="bi bi-people-fill text-primary me-2"></i><strong>Çalışan Sayısı:</strong> ${company.employeeCount || '-'} Kişi</div></div><h6 class="fw-bold text-primary mt-4 mb-3"><i class="bi bi-star-fill"></i> Puan ve Yorumlar</h6><div class="d-flex align-items-center mb-3 bg-light p-3 rounded border"><span class="fs-2 fw-bold text-warning me-3">${avgPoint > 0 ? avgPoint : '-'}</span><div class="d-flex flex-column"><span class="fs-5" style="line-height:1;">${avgPoint > 0 ? drawStars(avgPoint) : ''}</span></div></div><div style="max-height: 250px; overflow-y: auto;" class="pe-2">${commentsHtml}</div>${rateBtnHtml}</div><div class="col-md-5 bg-light rounded p-3"><h6 class="fw-bold text-primary mb-3"><i class="bi bi-briefcase-fill"></i> Açık İlanlar</h6><div style="max-height: 500px; overflow-y: auto;" class="pe-2">${jobsHtml}</div></div></div>`;
        
        document.getElementById('scpmName').innerText = company.name; new bootstrap.Modal(document.getElementById('sharedCompanyProfileModal')).show();
    } catch(e) {}
}

async function showCvModal(userId) {
    try {
        // Hata mesajını (alert) engellemek için burada doğrudan fetch kullanıyoruz
        const res = await fetch(`${API_BASE_URL}/cv/user/${userId}`, { headers: { 'Authorization': `Bearer ${localStorage.getItem('accessToken')}` } });
        
        // EĞER BACKEND'DEN HATA DÖNERSE (CV YOKSA) BOŞ EKRAN GÖSTER VE İŞLEMİ DURDUR
        if (!res.ok) {
            document.getElementById('cvModalName').innerHTML = `<i class="bi bi-person-badge text-secondary me-2"></i>Özgeçmiş Bulunamadı`; 
            document.getElementById('cvModalBody').innerHTML = `<div class="text-center py-5"><i class="bi bi-file-earmark-x text-muted" style="font-size: 4rem;"></i><h5 class="mt-3 text-muted fw-bold">Aday henüz CV oluşturmamış.</h5><p class="text-secondary small">Kullanıcının sistemde kayıtlı bir özgeçmiş bilgisi bulunmuyor.</p></div>`;
            new bootstrap.Modal(document.getElementById('cvModal')).show();
            return; 
        }

        // CV VARSA NORMAL ŞEKİLDE PARÇALA VE EKRANA BAS
        const text = await res.text();
        const responseData = JSON.parse(text);
        const cvData = responseData.data || responseData;

        // --- 1. KİŞİSEL BİLGİLER VE İLETİŞİM ---
        const user = cvData.user || {};
        const addr = user.address || {};
        const fullAddr = addr.city ? `${addr.neighborhood||''} Mah. ${addr.street||''} Sok. ${addr.district||''} / ${addr.city||''}` : 'Belirtilmemiş';
        
        document.getElementById('cvModalName').innerHTML = `<i class="bi bi-person-badge text-primary me-2"></i>${user.firstName || ''} ${user.lastName || ''}`; 
        
        let headerHtml = `
            <div class="row mb-4 pb-3 border-bottom">
                <div class="col-md-6 mb-2"><i class="bi bi-envelope text-muted me-2"></i><strong>Email:</strong> ${user.username || '-'}</div>
                <div class="col-md-6 mb-2"><i class="bi bi-telephone text-muted me-2"></i><strong>Telefon:</strong> ${user.phone || '-'}</div>
                <div class="col-md-6 mb-2"><i class="bi bi-calendar3 text-muted me-2"></i><strong>Doğum Tarihi:</strong> ${user.birthDate ? user.birthDate.split('-').reverse().join('.') : '-'}</div>
                <div class="col-md-6 mb-2"><i class="bi bi-geo-alt text-muted me-2"></i><strong>Adres:</strong> ${fullAddr}</div>
            </div>`;

        // --- 2. HAKKINDA ---
        let bioHtml = cvData.bio ? `<div class="mb-4"><h6 class="fw-bold text-primary border-bottom pb-1"><i class="bi bi-person-lines-fill me-2"></i>Hakkında</h6><p class="small text-muted">${cvData.bio}</p></div>` : '';

        // --- 3. EĞİTİM BİLGİLERİ ---
        const tm = { 'PRIMARY_SCHOOL': 'İlkokul', 'MIDDLE_SCHOOL': 'Ortaokul', 'HIGH_SCHOOL': 'Lise', 'ASSOCIATE': 'Ön Lisans', 'BACHELOR': 'Lisans', 'MASTER': 'Yüksek Lisans' };
        let sHtml = '';
        (cvData.schoolCv||[]).forEach(s => {
            const start = s.startDate ? s.startDate.split('-').reverse().join('.') : '?';
            const end = s.endDate ? s.endDate.split('-').reverse().join('.') : 'Devam Ediyor';
            sHtml += `<div class="mb-2 p-2 bg-light rounded border"><div class="fw-bold">${s.schoolName} <span class="badge bg-secondary ms-2">${tm[s.schoolType] || s.schoolType}</span></div><div class="small text-muted">${s.description || ''} ${s.point ? `(Puan: ${s.point})` : ''}</div><div class="small text-primary"><i class="bi bi-calendar-event me-1"></i>${start} - ${end}</div></div>`;
        });
        let eduHtml = sHtml ? `<div class="mb-4"><h6 class="fw-bold text-primary border-bottom pb-1"><i class="bi bi-mortarboard-fill me-2"></i>Eğitim Bilgileri</h6>${sHtml}</div>` : '';

        // --- 4. DENEYİMLER ---
        let eHtml = '';
        (cvData.experienceCv||[]).forEach(e => {
            const start = e.startDate ? e.startDate.split('-').reverse().join('.') : '?';
            const end = e.endDate ? e.endDate.split('-').reverse().join('.') : 'Devam Ediyor';
            eHtml += `<div class="mb-2 p-2 bg-light rounded border"><div class="fw-bold">${e.name}</div><div class="small text-primary"><i class="bi bi-calendar-event me-1"></i>${start} - ${end}</div></div>`;
        });
        let expHtml = eHtml ? `<div class="mb-4"><h6 class="fw-bold text-primary border-bottom pb-1"><i class="bi bi-briefcase-fill me-2"></i>Deneyimler</h6>${eHtml}</div>` : '';

        // --- 5. YABANCI DİLLER ---
        let lHtml = '';
        (cvData.languageCv||[]).forEach(l => {
            lHtml += `<span class="badge bg-light text-dark border p-2 me-2 mb-2"><i class="bi bi-globe me-1 text-primary"></i>${l.name} <strong class="ms-1 text-primary">${l.level}</strong></span>`;
        });
        let langHtml = lHtml ? `<div class="mb-3"><h6 class="fw-bold text-primary border-bottom pb-1"><i class="bi bi-translate me-2"></i>Yabancı Diller</h6>${lHtml}</div>` : '';

        // --- TÜMÜNÜ BİRLEŞTİR VE EKRANA BAS ---
        document.getElementById('cvModalBody').innerHTML = headerHtml + bioHtml + eduHtml + expHtml + langHtml; 
        
        new bootstrap.Modal(document.getElementById('cvModal')).show();
    } catch(e) {}
}

let currentRatingCompanyId = null;
function openRateModal(companyId, companyName) { currentRatingCompanyId = companyId; document.getElementById('companyRateModalName').innerText = `${companyName} - Yorum Yap`; new bootstrap.Modal(document.getElementById('companyRateModal')).show(); }
async function submitCompanyRate(e) { 
    e.preventDefault(); 
    try { await apiCall(`/companies/${currentRatingCompanyId}/comments`, 'PUT', { point: parseInt(document.getElementById('ratePoint').value), comment: document.getElementById('rateText').value }); bootstrap.Modal.getInstance(document.getElementById('companyRateModal')).hide(); alert("Değerlendirme eklendi!"); loadPublicView(); } catch(e) {}
}