import http from './http'

let cachedWords: string[] = []
let cacheTime = 0

export async function getActiveSensitiveWords(): Promise<string[]> {
  const now = Date.now()
  // Cache for 5 minutes
  if (cachedWords.length > 0 && now - cacheTime < 300000) {
    return cachedWords
  }
  try {
    const data: any = await http.get('/sensitive-words/active')
    cachedWords = Array.isArray(data) ? data : []
    cacheTime = now
  } catch {
    // ignore, use cached
  }
  return cachedWords
}

export function checkSensitiveWords(text: string, words: string[]): string[] {
  if (!text || !words.length) return []
  const lower = text.toLowerCase()
  return words.filter(w => lower.includes(w.toLowerCase()))
}

export function clearSensitiveCache() {
  cachedWords = []
  cacheTime = 0
}
