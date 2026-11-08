(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const canvas = $('game'), ctx = canvas.getContext('2d');
  const W = 1280, H = 720, ROUND_SECONDS = 240;
  const background = new Image();
  const music = new Audio('assets/nhacnen.mp3?v=b97a75930007');
  music.loop = true; music.volume = .35;
  let mode = 'loading', elapsed = 0, score = 0, spawnTime = 2.4, last = 0, musicEnabled = true, character = 'Nam';
  let bubbles = [], flashes = [];
  let touchHeld = false, pointer = null;
  const rand = (a,b) => a + Math.random() * (b-a);
  function playMusic() { if (musicEnabled && mode === 'playing') music.play().catch(() => {}); }
  function hud() {
    $('score').textContent = String(score).padStart(3,'0');
    $('count').textContent = score + ' b\u00f3ng';
    const time = Math.max(0, Math.ceil(ROUND_SECONDS-elapsed));
    $('time').textContent = String(Math.floor(time/60)).padStart(2,'0') + ':' + String(time%60).padStart(2,'0');
  }
  function start() {
    if (mode === 'paused') { resume(); return; }
    score = 0; elapsed = 0; spawnTime = 2.4; bubbles = []; flashes = []; pointer = null; touchHeld = false;
    mode = 'playing'; $('overlay').classList.add('hidden'); $('pause').disabled = false; hud(); playMusic();
  }
  function pause() {
    if (mode !== 'playing') return;
    mode = 'paused'; touchHeld = false; music.pause();
    $('overlayTag').textContent = 'T\u1ea0M D\u1eeaNG';
    $('overlayTitle').textContent = 'Ngh\u1ec9 m\u1ed9t ch\u00fat nh\u00e9!';
    $('overlayText').textContent = 'Nh\u1ea5n ti\u1ebfp t\u1ee5c khi b\u1ea1n s\u1eb5n s\u00e0ng.';
    $('characters').hidden = true; $('start').textContent = 'Ti\u1ebfp t\u1ee5c'; $('overlay').classList.remove('hidden');
  }
  function resume() { mode = 'playing'; $('overlay').classList.add('hidden'); playMusic(); }
  function finish() {
    mode = 'finished'; touchHeld = false; music.pause(); $('pause').disabled = true;
    $('overlayTag').textContent = 'HO\u00c0N TH\u00c0NH'; $('overlayTitle').textContent = 'B\u1ea1n b\u1eaft \u0111\u01b0\u1ee3c ' + score + ' bong b\u00f3ng!';
    $('overlayText').textContent = 'M\u1ed7i bong b\u00f3ng mang v\u1ec1 1 \u0111i\u1ec3m. C\u00f9ng ch\u01a1i l\u1ea1i nh\u00e9!';
    $('characters').hidden = false; $('start').textContent = 'Ch\u01a1i l\u1ea1i'; $('overlay').classList.remove('hidden');
    try { const history = JSON.parse(localStorage.getItem('river-bubble-history') || '[]'); history.unshift({date:new Date().toISOString(),character,bubbles:score,score,version:'2.1'}); localStorage.setItem('river-bubble-history',JSON.stringify(history.slice(0,50))); } catch {}
  }
  function catchBubbles() {
    if (!pointer || mode !== 'playing') return;
    for (let i = bubbles.length-1; i >= 0; i--) {
      const b = bubbles[i];
      if (pointer.x >= b.x-5 && pointer.x <= b.x+45 && pointer.y >= b.y-5 && pointer.y <= b.y+45) {
        flashes.push({x:b.x,y:b.y,life:.6}); bubbles.splice(i,1); score++;
      }
    }
    hud();
  }
  function update(dt) {
    elapsed = Math.min(ROUND_SECONDS,elapsed+dt);
    if (elapsed >= ROUND_SECONDS) { hud(); finish(); return; }
    bubbles.forEach(b => b.y += 40*dt); bubbles = bubbles.filter(b => b.y <= H);
    if (touchHeld) catchBubbles();
    spawnTime += dt;
    if (spawnTime >= 2.4) { bubbles.push({x:rand(0,W-40),y:rand(60,H-40)}); spawnTime %= 2.4; }
    flashes.forEach(f => {f.life -= dt; f.y -= 35*dt;}); flashes = flashes.filter(f => f.life > 0); hud();
  }
  function draw() {
    ctx.clearRect(0,0,W,H); if (background.complete && background.naturalWidth) ctx.drawImage(background,0,0,W,H);
    bubbles.forEach(b => {
      ctx.beginPath(); ctx.arc(b.x+20,b.y+20,16,0,Math.PI*2); ctx.fillStyle='rgba(77,204,255,.5)'; ctx.fill();
      ctx.strokeStyle='rgba(26,230,255,.95)'; ctx.lineWidth=2; ctx.stroke();
      ctx.beginPath(); ctx.arc(b.x+15,b.y+14,4,0,Math.PI*2); ctx.fillStyle='rgba(255,255,255,.85)'; ctx.fill();
    });
    flashes.forEach(f => {ctx.save();ctx.globalAlpha=f.life/.6;ctx.fillStyle='#164f35';ctx.font='bold 26px system-ui';ctx.fillText('+1',f.x,f.y);ctx.restore();});
  }
  function frame(t) { const dt = last ? Math.min((t-last)/1000,.05) : 0; last=t; if(mode==='playing')update(dt); if(mode!=='loading' && mode!=='error')draw(); requestAnimationFrame(frame); }
  function locate(e) { const r=canvas.getBoundingClientRect(); pointer={x:(e.clientX-r.left)*W/r.width,y:(e.clientY-r.top)*H/r.height}; }
  canvas.addEventListener('pointerdown',e=>{if(mode!=='playing')return;e.preventDefault();canvas.setPointerCapture(e.pointerId);locate(e);touchHeld=true;catchBubbles();});
  canvas.addEventListener('pointermove',e=>{locate(e);if(touchHeld)catchBubbles();});
  ['pointerup','pointercancel','lostpointercapture'].forEach(name=>canvas.addEventListener(name,()=>{touchHeld=false;}));
  $('start').addEventListener('click',start); $('pause').addEventListener('click',pause);
  $('music').addEventListener('click',()=>{musicEnabled=!musicEnabled;$('music').setAttribute('aria-pressed',String(musicEnabled));$('music').textContent=musicEnabled?'T\u1eaft nh\u1ea1c':'B\u1eadt nh\u1ea1c';if(musicEnabled)playMusic();else music.pause();});
  window.addEventListener('keydown',e=>{if(e.key==='Escape'){if(mode==='playing')pause();else if(mode==='paused')resume();}});
  window.addEventListener('blur',pause); document.addEventListener('visibilitychange',()=>{if(document.hidden)pause();});
  document.querySelectorAll('[data-character]').forEach(button=>button.addEventListener('click',()=>{character=button.dataset.character;document.querySelectorAll('[data-character]').forEach(b=>{b.classList.toggle('chosen',b===button);b.setAttribute('aria-pressed',String(b===button));});}));
  background.onload=()=>{mode='ready';$('start').disabled=false;$('start').textContent='B\u1eaft \u0111\u1ea7u ch\u01a1i';draw();};
  background.onerror=()=>{mode='error';$('loadError').textContent='Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c h\u00ecnh n\u1ec1n. H\u00e3y t\u1ea3i l\u1ea1i trang.';};
  background.src='assets/background.jpg'; requestAnimationFrame(frame);
})();
