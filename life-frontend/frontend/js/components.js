/* ================================================
   CWS.life — components.js (TOP STRIP FIXED)
   ================================================ */

async function loadComponent(id, file) {
  try {
    const res  = await fetch(file);
    const html = await res.text();
    const el   = document.getElementById(id);
    if (el) el.innerHTML = html;
    document.dispatchEvent(new CustomEvent('componentLoaded', { detail: { id } }));
  } catch (err) {
    console.warn('Could not load component:', file, err);
  }
}

function setActiveLink() {
  const current = window.location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('.nav-links > li > a').forEach(link => {
    const href = link.getAttribute('href').split('/').pop().split('#')[0];
    if (href === current) link.parentElement.classList.add('active');
  });
}

// ══════════════════════════════════
// NAVBAR SCROLL LOGIC (FIXED)
// ══════════════════════════════════
function initNavScroll() {
  const nav   = document.getElementById('cwsNav');
  const strip = document.getElementById('strip');
  const ann   = document.getElementById('ann');
  const prog  = document.getElementById('prog');
  const btt   = document.getElementById('btt');
  
  if (!nav) return;

  // Create sentinel
  const sentinel = document.createElement('div');
  sentinel.style.cssText = 'position: absolute; top: 0; height: 1px; width: 100%; pointer-events: none;';
  document.body.prepend(sentinel);

  // Observer Logic
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      const isScrolled = !entry.isIntersecting;
      
      nav.classList.toggle('scrolled', isScrolled);
      nav.classList.toggle('nav-sticky', isScrolled); // Added class for CSS hook
      
      // FIX: Strip will hide only when scrolled
      if (strip) strip.classList.toggle('hide', isScrolled);
      if (ann && !window._annClosed) ann.classList.toggle('hide', isScrolled);
      
      if (btt) btt.classList.toggle('show', isScrolled);
    });
  }, { 
    threshold: 0, 
    rootMargin: '-1px 0px 0px 0px' // Triggers immediately on 1px scroll
  });

  observer.observe(sentinel);

  // Progress Bar
  let ticking = false;
  window.addEventListener('scroll', () => {
    if (!ticking) {
      window.requestAnimationFrame(() => {
        if (prog) {
          const sy = window.scrollY;
          const total = document.documentElement.scrollHeight - window.innerHeight;
          if (total > 0) prog.style.width = (sy / total * 100) + '%';
        }
        ticking = false;
      });
      ticking = true;
    }
  }, { passive: true });
}

window.closeAnn = function () {
  const ann = document.getElementById('ann');
  if (ann) ann.classList.add('hide');
  window._annClosed = true;
};

window.toggleMob = function () {
  const ham     = document.getElementById('ham');
  const mobMenu = document.getElementById('mobMenu');
  if (!ham || !mobMenu) return;
  ham.classList.toggle('open');
  mobMenu.classList.toggle('open');
  document.body.style.overflow = mobMenu.classList.contains('open') ? 'hidden' : '';
};

window.toggleSub = function (e, id) {
  e.preventDefault();
  const el = document.getElementById(id);
  if (el) el.classList.toggle('open');
};

function initReveal() {
  const ro = new IntersectionObserver(entries => {
    entries.forEach(e => {
      if (e.isIntersecting) { e.target.classList.add('in'); ro.unobserve(e.target); }
    });
  }, { threshold: 0.08 });
  document.querySelectorAll('.rv').forEach(el => ro.observe(el));
}

async function init() {
  await loadComponent('navbar-placeholder', 'components/navbar.html');
  await loadComponent('footer-placeholder', 'components/footer.html');
  
  setActiveLink();
  initNavScroll();
  initReveal();
  
  if (typeof pageInit === 'function') pageInit();
}

document.addEventListener('DOMContentLoaded', init);